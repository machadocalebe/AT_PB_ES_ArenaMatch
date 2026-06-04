package br.infnet.arenamatch.reservas;

import br.infnet.arenamatch.quadras.Quadra;
import br.infnet.arenamatch.quadras.QuadraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final QuadraRepository quadraRepository;

    // Injeção de dependência via construtor (Best Practice)
    public ReservaService(ReservaRepository reservaRepository, QuadraRepository quadraRepository) {
        this.reservaRepository = reservaRepository;
        this.quadraRepository = quadraRepository;
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

        return reservaRepository.save(novaReserva);
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