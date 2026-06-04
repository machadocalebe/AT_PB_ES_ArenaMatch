package br.infnet.arenamatch.quadras;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class QuadraService {

    private final QuadraRepository quadraRepository;

    public QuadraService(QuadraRepository quadraRepository) {
        this.quadraRepository = quadraRepository;
    }

    public Quadra criar(QuadraRequestDTO dto) {
        Quadra quadra = new Quadra();
        quadra.setNome(dto.getNome());
        quadra.setPrecoHora(dto.getPrecoHora());
        quadra.setEmManutencao(false); // Nasce sempre pronta para uso
        return quadraRepository.save(quadra);
    }

    // Atualizar Quadra
    public Quadra atualizar(Long id, QuadraRequestDTO dto) {
        Quadra quadra = quadraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quadra não encontrada!"));
        quadra.setNome(dto.getNome());
        quadra.setPrecoHora(dto.getPrecoHora());
        return quadraRepository.save(quadra);
    }

    // Deletar Quadra
    public void deletar(Long id) {
        if (!quadraRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quadra não encontrada!");
        }
        try {
            quadraRepository.deleteById(id);
        } catch (Exception e) {
            // Se a quadra já tiver reservas atreladas, o banco de dados vai bloquear a exclusão.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível excluir uma quadra que possui reservas.");
        }
    }

    public List<Quadra> listarTodas() {
        return quadraRepository.findAll();
    }

    // Alterna o status de manutenção
    public Quadra alternarManutencao(Long id) {
        Quadra quadra = quadraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quadra não encontrada!"));

        quadra.setEmManutencao(!quadra.isEmManutencao());
        return quadraRepository.save(quadra);
    }
}