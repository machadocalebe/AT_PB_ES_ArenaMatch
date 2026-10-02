package br.infnet.arenamatch.eventos;

import br.infnet.arenamatch.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoWorker {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoWorker.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVA)
    public void processarNovaReserva(ReservaCriadaEvent event) {
        logger.info("=========================================");
        logger.info("[WORKER DE NOTIFICAÇÃO] Nova reserva detectada!");
        logger.info("Enviando e-mail de confirmação para o locatário: {}", event.getNomeLocatario());
        logger.info("Data do jogo: {}", event.getDataHoraInicio());
        logger.info("Reserva ID: {}", event.getReservaId());
        logger.info("=========================================");
        // Aqui entraria a lógica real de envio de e-mail (ex: JavaMailSender)
    }
}
