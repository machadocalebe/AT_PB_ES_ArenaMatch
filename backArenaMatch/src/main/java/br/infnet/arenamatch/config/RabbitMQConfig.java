package br.infnet.arenamatch.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "arenamatch.exchange";
    public static final String QUEUE_AVALIACAO = "avaliacao.queue";
    public static final String ROUTING_KEY_AVALIACAO = "avaliacao.created";

    public static final String QUEUE_RESERVA = "reserva.queue";
    public static final String ROUTING_KEY_RESERVA = "reserva.criada";

    public static final String QUEUE_QUADRA = "quadra.queue";
    public static final String ROUTING_KEY_QUADRA = "quadra.manutencao";

    @Bean
    public Queue avaliacaoQueue() {
        return new Queue(QUEUE_AVALIACAO, true); // true indica que a fila é durável
    }

    @Bean
    public Queue reservaQueue() {
        return new Queue(QUEUE_RESERVA, true);
    }

    @Bean
    public Queue quadraQueue() {
        return new Queue(QUEUE_QUADRA, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingAvaliacao(Queue avaliacaoQueue, DirectExchange exchange) {
        return BindingBuilder.bind(avaliacaoQueue).to(exchange).with(ROUTING_KEY_AVALIACAO);
    }

    @Bean
    public Binding bindingReserva(Queue reservaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(reservaQueue).to(exchange).with(ROUTING_KEY_RESERVA);
    }

    @Bean
    public Binding bindingQuadra(Queue quadraQueue, DirectExchange exchange) {
        return BindingBuilder.bind(quadraQueue).to(exchange).with(ROUTING_KEY_QUADRA);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
