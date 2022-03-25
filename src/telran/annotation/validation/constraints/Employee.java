package telran.annotation.validation.constraints;


public class Employee {

	@Patern(value="[A-Z][a-z]+")
	String name = "haim";
	@Min(value=18)
	@Max(value=120)
	double age = 15.0;
	double salary = 5000;
	@Valid
	Address address = new Address("Paris", "Didot", 45.0);
}
