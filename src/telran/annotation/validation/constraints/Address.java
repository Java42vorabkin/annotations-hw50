package telran.annotation.validation.constraints;

public class Address {
	
	@Patern(value="[A-Z][a-z]+")
	String city="Paris";
	@Patern(value="[A-Z][\s[a-z]]+")
	String street = "Didot";
	@Max(value=120)
	@Min(value=1)
	double houseNumber = 23;
	Address(String city, String street, Double number) {
		this.city = city;
		this.street = street;
		this.houseNumber = number;
	}
	Address() {}
}
