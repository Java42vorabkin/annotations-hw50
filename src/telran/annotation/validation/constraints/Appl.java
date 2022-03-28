package telran.annotation.validation.constraints;

import java.util.List;

public class Appl {
	public static void main(String[] args) {
		Validator vaildator = new Validator();
		Address address = new Address("Paris", "Didot", 23);
		List<String> results1 = vaildator.validate(address);
		System.out.println("Class "+address.getClass().getSimpleName()+" : "+"Amount of violations is "+results1.size());
		results1.forEach(str -> System.out.println(str));
		System.out.println("-----------------------------");
		Employee employee = new Employee("haim", 15, 5000, address);
		List<String> results = vaildator.validate(employee);
		System.out.println("Class "+employee.getClass().getSimpleName()+" : "+"Amount of violations is "+results.size());
		results.forEach(str -> System.out.println(str));
	}
}
