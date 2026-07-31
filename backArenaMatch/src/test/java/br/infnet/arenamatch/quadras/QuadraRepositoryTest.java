package br.infnet.arenamatch.quadras;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuadraRepositoryTest {

    @Autowired
    private QuadraRepository quadraRepository;

    @Test
    @DisplayName("Deve salvar uma quadra com sucesso gerando o ID")
    void deveSalvarQuadra() {
        Quadra quadra = new Quadra("Quadra Society Diamante", 250.0);
        Quadra salva = quadraRepository.save(quadra);

        assertNotNull(salva.getId(), "O ID não deveria ser nulo após salvar");
        assertEquals("Quadra Society Diamante", salva.getNome());
        assertFalse(salva.isEmManutencao(), "A quadra deve nascer com manutenção falsa por padrão");
    }

    @Test
    @DisplayName("Deve buscar uma quadra existente pelo ID")
    void deveBuscarQuadraPorId() {
        Quadra quadra = quadraRepository.save(new Quadra("Quadra Busca", 150.0));

        Optional<Quadra> encontrada = quadraRepository.findById(quadra.getId());

        assertTrue(encontrada.isPresent());
        assertEquals(quadra.getId(), encontrada.get().getId());
    }

    @Test
    @DisplayName("Deve atualizar os dados de uma quadra persistida")
    void deveAtualizarQuadra() {
        Quadra quadra = quadraRepository.save(new Quadra("Quadra Antiga", 100.0));

        quadra.setNome("Quadra Atualizada");
        quadra.setPrecoHora(200.0);
        quadra.setEmManutencao(true);
        Quadra atualizada = quadraRepository.save(quadra);

        assertEquals("Quadra Atualizada", atualizada.getNome());
        assertEquals(200.0, atualizada.getPrecoHora());
        assertTrue(atualizada.isEmManutencao());
    }

    @Test
    @DisplayName("Deve deletar uma quadra com sucesso")
    void deveDeletarQuadra() {
        Quadra quadra = quadraRepository.save(new Quadra("Quadra Deletar", 100.0));
        Long id = quadra.getId();

        quadraRepository.deleteById(id);
        Optional<Quadra> deletada = quadraRepository.findById(id);

        assertTrue(deletada.isEmpty(), "A quadra deveria ter sido removida do banco");
    }
}