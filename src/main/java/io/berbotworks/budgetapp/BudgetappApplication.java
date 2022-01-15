package io.berbotworks.budgetapp;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.berbotworks.budgetapp.models.Details;
import io.berbotworks.budgetapp.models.Expense;
import io.berbotworks.budgetapp.models.History;
import io.berbotworks.budgetapp.models.TableEntry;
import io.berbotworks.budgetapp.models.User;
import io.berbotworks.budgetapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class BudgetappApplication implements CommandLineRunner {
	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BudgetappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User("raja@gmail.com", "$2a$12$HoB9sh0m23D7Q2AipC5QPeMiaWpoopCcNQgYFy8aARwvUJ0OJU9s6",
				Arrays.asList("ROLE_DEV", "ROLE_USER"));
		BigDecimal zero = new BigDecimal("100");
		Expense expense = new Expense("maggi", zero, "1 10");
		History history = new History("10 22", zero, zero, zero);
		TableEntry tableEntry = new TableEntry("1 10", zero);
		Details details = new Details().budget(zero).spent(zero).savings(zero);
		details.getExpenses().add(expense);
		details.getHistorySet().add(history);
		details.getTableEntries().add(tableEntry);
		expense.setDetails(details);
		history.setDetails(details);
		tableEntry.setDetails(details);
		user.setDetails(details);
		details.setUser(user);
		userRepository.save(user);

		User user1 = new User("raja1", "123", Arrays.asList("ROLE_USER"));
		BigDecimal zero1 = new BigDecimal("100");
		Expense expense1 = new Expense("maggi", zero, "1 10");
		History history1 = new History("10 22", zero, zero, zero);
		TableEntry tableEntry1 = new TableEntry("1 10", zero);
		Details details1 = new Details().budget(zero1).spent(zero1).savings(zero1);
		details1.getExpenses().add(expense1);
		details1.getHistorySet().add(history1);
		details1.getTableEntries().add(tableEntry1);
		expense1.setDetails(details1);
		history1.setDetails(details1);
		tableEntry1.setDetails(details1);
		user1.setDetails(details1);
		details1.setUser(user1);
		userRepository.save(user1);
	}

}
