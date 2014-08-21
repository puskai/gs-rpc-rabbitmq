package hello;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class ServerApplication {

	final static String queueName = Constants.queueName;
	final static String exchangeName = Constants.exchangeName;
	final static String bindingName = Constants.bindingName;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		return connectionFactory;
	}

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(exchangeName, true, false);
	}

	@Bean
	Queue queue() {
		return new Queue(queueName, true);
	}

	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(bindingName);
	}

	@Bean
	public CalculationService service() {
		return new CalculationServiceImpl();
	}

	@Bean
	public AmqpInvokerServiceExporter listener() {
		AmqpInvokerServiceExporter serviceExporter = new AmqpInvokerServiceExporter();
		serviceExporter.setService(CalculationService.class);
		serviceExporter.setService(service());
		serviceExporter.setAmqpTemplate(rabbitTemplate);
		return serviceExporter;
	}

	@Bean
	SimpleMessageListenerContainer container() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(queueName);
		container.setMessageListener(listener());
		return container;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ServerApplication.class, args);
	}
}
