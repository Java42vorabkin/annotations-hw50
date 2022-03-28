package telran.annotation.validation.constraints;


public class Employee {

	@Patern(value="[A-Z][a-z]+")
	String name;
	@Min(value=18)
	@Max(value=120)
	int age; 	// !!! #6
	int salary; // !!! #7
	@Valid
	Address address;
	Employee(String name, int age, int salary, Address address) {
		this.name = name;
		this.age = age;
		this.salary = salary;
		this.address = new Address(address.city, address.street, address.houseNumber);
	}
}
