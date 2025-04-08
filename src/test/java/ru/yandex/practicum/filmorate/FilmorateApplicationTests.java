package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {
	private Validator validator;

	@BeforeEach
	void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void shouldFailWhenEmailIsBlank() {
		User user = new User();
		user.setEmail(""); // некорректный email
		user.setLogin("privet");
		user.setBirthday(LocalDate.of(2000, 1, 1));

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());

		ConstraintViolation<User> violation = violations.iterator().next();

		assertEquals("must not be blank", violation.getMessage());
		assertEquals("email", violation.getPropertyPath().toString());
	}
}
