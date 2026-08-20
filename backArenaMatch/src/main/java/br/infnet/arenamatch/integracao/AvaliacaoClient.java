package br.infnet.arenamatch.integracao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "avaliacoes-ms", url = "http://localhost:8081/api/avaliacoes")
public interface AvaliacaoClient {

    @GetMapping("/quadra/{quadraId}")
    List<AvaliacaoDTO> buscarNotasDaQuadra(@PathVariable("quadraId") Long quadraId);

    // Novo método para enviar a avaliação
    @PostMapping
    AvaliacaoDTO salvarAvaliacao(@RequestBody AvaliacaoDTO avaliacao);
}