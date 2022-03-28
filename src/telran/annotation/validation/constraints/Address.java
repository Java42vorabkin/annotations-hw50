package telran.annotation.validation.constraints;

public class Address {
	
	@Patern(value="[A-Z][a-z]+")
	String city;
	@Patern(value="[A-Z][\s[a-z]]+")
	String street;
	@Max(value=120)
	@Min(value=1)
	int houseNumber;  // !!! #8
	Address(String city, String street, int number) {
		this.city = city;
		this.street = street;
		this.houseNumber = number;
	}
	Address() {}
}
