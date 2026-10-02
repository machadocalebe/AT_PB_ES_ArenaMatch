package br.infnet.arenamatch.eventos;

import br.infnet.arenamatch.config.RabbitMQConfig;
import br.infnet.arenamatch.reservas.ReservaRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ManutencaoWorker {

    private static final Logger logger = LoggerFactory.getLogger(ManutencaoWorker.class);
    private final ReservaRepository reservaRepository;

    public ManutencaoWorker(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_QUADRA)
    @Transactional
    public void processarManutencaoQuadra(QuadraEmManutencaoEvent event) {
        logger.warn("=========================================");
        logger.warn("[WORKER DE MANUTENÇÃO] Quadra interditada: {}", event.getNomeQuadra());
        logger.warn("Cancelando todas as reservas atreladas à quadra ID: {}...", event.getQuadraId());

        reservaRepository.deleteByQuadraId(event.getQuadraId());

        logger.warn("Reservas canceladas com sucesso. Clientes notificados do cancelamento (simulação).");
        logger.warn("=========================================");
    }
}
