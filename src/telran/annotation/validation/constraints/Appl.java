package telran.annotation.validation.constraints;

import java.util.List;

public class Appl {
	public static void main(String[] args) {
		Validator vaildator = new Validator();
		List<String> results1 = vaildator.validate(new Address());
		System.out.println("Amount of violations is "+results1.size());
		results1.forEach(str -> System.out.println(str));
		System.out.println("-----------------------------");
		List<String> results = vaildator.validate(new Employee());
		System.out.println("Amount of violations is "+results.size());
		results.forEach(str -> System.out.println(str));
	}
}
