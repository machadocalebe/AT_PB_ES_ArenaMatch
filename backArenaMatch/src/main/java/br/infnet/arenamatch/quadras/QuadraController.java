package br.infnet.arenamatch.quadras;

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
}