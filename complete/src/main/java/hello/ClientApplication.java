package hello;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@Configuration
public class ClientApplication {

	final static String queueName = Constants.queueName;
	final static String exchangeName = Constants.exchangeName;
	final static String bindingName = Constants.bindingName;

	@Bean
	public ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor() {
		return new ScheduledAnnotationBeanPostProcessor();
	}

	@Bean
	public Client sender() {
		return new Client();
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		return connectionFactory;
	}

	@Bean
	public RabbitAdmin admin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setExchange(exchangeName);
		template.setRoutingKey(bindingName);
		template.setReplyTimeout(2000);
		return template;
	}

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
	public AmqpProxyFactoryBean amqpProxyFactoryBean() {
		AmqpProxyFactoryBean amqpProxyFactoryBean = new AmqpProxyFactoryBean();
		amqpProxyFactoryBean.setServiceInterface(CalculationService.class);
		amqpProxyFactoryBean.setAmqpTemplate(rabbitTemplate());
		return amqpProxyFactoryBean;
	}

	@Bean
	public CalculationService calculationService() throws Exception {
		return (CalculationService) amqpProxyFactoryBean().getObject();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ClientApplication.class, args);
	}
}
