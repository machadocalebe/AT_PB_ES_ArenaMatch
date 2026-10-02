package br.infnet.arenamatch.reservas;

import br.infnet.arenamatch.quadras.Quadra;
import br.infnet.arenamatch.quadras.QuadraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import br.infnet.arenamatch.config.RabbitMQConfig;
import br.infnet.arenamatch.eventos.ReservaCriadaEvent;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final QuadraRepository quadraRepository;
    private final RabbitTemplate rabbitTemplate;

    // Injeção de dependência via construtor (Best Practice)
    public ReservaService(ReservaRepository reservaRepository, QuadraRepository quadraRepository, RabbitTemplate rabbitTemplate) {
        this.reservaRepository = reservaRepository;
        this.quadraRepository = quadraRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Reserva criarReserva(ReservaRequestDTO dto) {
        // Fail-Fast: Verifica se a quadra existe
        Quadra quadra = quadraRepository.findById(dto.getQuadraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quadra não encontrada!"));

        // Verifica se a quadra está em manutenção
        if (quadra.isEmManutencao()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta quadra está interditada para manutenção!");
        }

        // Fail-Fast: Verifica choque de horário
        boolean horarioOcupado = reservaRepository.existsByQuadraIdAndDataHoraInicio(quadra.getId(), dto.getDataHoraInicio());
        if (horarioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta quadra já está reservada para este horário!");
        }

        // Cria e salva
        Reserva novaReserva = new Reserva();
        novaReserva.setNomeLocatario(dto.getNomeLocatario());
        novaReserva.setDataHoraInicio(dto.getDataHoraInicio());
        novaReserva.setQuadra(quadra);

        Reserva reservaSalva = reservaRepository.save(novaReserva);

        // Dispara o evento de Reserva Criada
        ReservaCriadaEvent event = new ReservaCriadaEvent(
                reservaSalva.getId(),
                reservaSalva.getNomeLocatario(),
                quadra.getId(),
                reservaSalva.getDataHoraInicio()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_RESERVA, event);

        return reservaSalva;
    }

    public void deletar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada!");
        }
        reservaRepository.deleteById(id);
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }
}