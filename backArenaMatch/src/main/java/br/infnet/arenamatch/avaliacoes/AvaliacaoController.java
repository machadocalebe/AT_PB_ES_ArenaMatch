package br.infnet.arenamatch.avaliacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository repository;

    @GetMapping("/quadra/{quadraId}")
    public List<Avaliacao> listarPorQuadra(@PathVariable Long quadraId) {
        return repository.findByQuadraId(quadraId);
    }
}
