package br.infnet.arenamatch.eventos;

import br.infnet.arenamatch.avaliacoes.Avaliacao;
import br.infnet.arenamatch.avaliacoes.AvaliacaoRepository;
import br.infnet.arenamatch.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AvaliacaoWorker {

    @Autowired
    private AvaliacaoRepository repository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_AVALIACAO)
    public void processarNovaAvaliacao(Map<String, Object> payload) {
        System.out.println("[WORKER DE AVALIAÇÃO] Recebendo nova avaliação via RabbitMQ...");
        
        Long quadraId = Long.valueOf(payload.get("quadraId").toString());
        String autor = (String) payload.get("autor");
        Integer nota = (Integer) payload.get("nota");
        String comentario = (String) payload.get("comentario");

        Avaliacao avaliacao = new Avaliacao(quadraId, autor, nota, comentario);
        repository.save(avaliacao);
        
        System.out.println("[WORKER DE AVALIAÇÃO] Avaliação salva com sucesso no banco de dados!");
    }
}
