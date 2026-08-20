package br.infnet.arenamatch.quadras;

import br.infnet.arenamatch.integracao.AvaliacaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quadras")
@CrossOrigin(origins = "*")
public class QuadraController {

    private final QuadraService quadraService;

    public QuadraController(QuadraService quadraService) {
        this.quadraService = quadraService;
    }

    @GetMapping
    public ResponseEntity<List<Quadra>> listar() {
        return ResponseEntity.ok(quadraService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Quadra> criar(@RequestBody QuadraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quadraService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quadra> atualizar(@PathVariable Long id, @RequestBody QuadraRequestDTO dto) {
        return ResponseEntity.ok(quadraService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        quadraService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Usamos PATCH pois estamos alterando apenas um campo parcial da entidade
    @PatchMapping("/{id}/manutencao")
    public ResponseEntity<Quadra> alternarManutencao(@PathVariable Long id) {
        return ResponseEntity.ok(quadraService.alternarManutencao(id));
    }

    // Adicione a injeção do cliente Feign no topo do Controller:
    @Autowired
    private AvaliacaoClient avaliacaoClient;

    // --- Endpoints de Integração com o Microsserviço ---

    @GetMapping("/{id}/avaliacoes")
    public ResponseEntity<?> listarAvaliacoes(@PathVariable Long id) {
        // O ArenaMatch pede a lista de notas para o microsserviço
        return ResponseEntity.ok(avaliacaoClient.buscarNotasDaQuadra(id));
    }

    @PostMapping("/{id}/avaliacoes")
    public ResponseEntity<?> avaliarQuadra(@PathVariable Long id, @RequestBody br.infnet.arenamatch.integracao.AvaliacaoDTO dto) {
        dto.setQuadraId(id);
        // O ArenaMatch recebe a nota do React e repassa para o microsserviço
        return ResponseEntity.ok(avaliacaoClient.salvarAvaliacao(dto));
    }
}