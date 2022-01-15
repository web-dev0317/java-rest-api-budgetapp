package io.berbotworks.budgetapp.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import io.berbotworks.budgetapp.models.Expense;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    Set<Expense> findAllByDateAndDetails_Id(String date, Long detailsId);

    Set<Expense> findAllByDetails_Id(Long detailsId);
}
