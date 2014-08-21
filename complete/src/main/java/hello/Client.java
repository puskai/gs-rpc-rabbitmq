package hello;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class Client {

	private static final long MIN = 10L;
	private static final long MAX = 100L;

	@Autowired
	private CalculationService calculationService;

	@Scheduled(fixedDelay = 1L)
	public void sum() {
		Random r = new Random();
		long randomValue1 = MIN + ((long) (r.nextDouble() * (MAX - MIN)));
		long randomValue2 = MIN + ((long) (r.nextDouble() * (MAX - MIN)));
		double result = calculationService.sum(randomValue1, randomValue2);
		System.out.println(randomValue1 + " + " + randomValue2 + " =" + result);
	}

	@Scheduled(fixedDelay = 5L)
	public void multiply() {
		Random r = new Random();
		long randomValue1 = MIN + ((long) (r.nextDouble() * (MAX - MIN)));
		long randomValue2 = MIN + ((long) (r.nextDouble() * (MAX - MIN)));
		double result = calculationService.multiply(randomValue1, randomValue2);
		System.out.println(randomValue1 + " * " + randomValue2 + " =" + result);
	}

}
