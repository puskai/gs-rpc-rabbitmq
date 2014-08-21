package hello;

public class CalculationServiceImpl implements CalculationService {

	@Override
	public double sum(double value1, double value2) {
		return value1 + value2;
	}

	@Override
	public double multiply(double value1, double value2) {
		return value1 * value2;
	}
}
