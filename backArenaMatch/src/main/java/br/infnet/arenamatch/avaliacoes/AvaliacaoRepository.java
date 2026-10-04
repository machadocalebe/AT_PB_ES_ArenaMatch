package br.infnet.arenamatch.avaliacoes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByQuadraId(Long quadraId);
}
