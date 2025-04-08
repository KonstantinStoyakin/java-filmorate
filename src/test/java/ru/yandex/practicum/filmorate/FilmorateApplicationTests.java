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
import static org.junit.jupiter.api.Assertions.assertFalse;

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

		assertEquals("не должно быть пустым", violation.getMessage());
		assertEquals("email", violation.getPropertyPath().toString());
	}
}
