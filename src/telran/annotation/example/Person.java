package telran.annotation.example;

import java.time.LocalDate;

import telran.annotation.*;

@Table

public class Person {
@Id
	int id;

	String name;
	@Column(name="birth_date")
	LocalDate birthDate;
}
