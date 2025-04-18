package spring.myproject.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String CHAT_QUEUE = "chat_queue";
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String CHAT_ROUTING_KEY = "chat";
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Queue chatQueue() {
        return new Queue(CHAT_QUEUE);
    }
    @Bean
    public Binding chatBinding() {
        return BindingBuilder.bind(chatQueue()).to(topicExchange()).with(CHAT_ROUTING_KEY);
    }

}
