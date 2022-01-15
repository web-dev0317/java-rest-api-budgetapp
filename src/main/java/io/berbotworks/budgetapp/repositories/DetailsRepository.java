package io.berbotworks.budgetapp.repositories;

import io.berbotworks.budgetapp.models.Details;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DetailsRepository extends CrudRepository<Details, Long> {
    Optional<Details> findByUser_Id(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Details d SET d.budget = ?1 WHERE d.id = ?2")
    void updateBudgetById(BigDecimal budget, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Details d SET d.spent = ?1 WHERE d.id = ?2")
    void updateSpentById(BigDecimal spent, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Details d SET d.savings = ?1 WHERE d.id = ?2")
    void updateSavingsById(BigDecimal savings, Long id);

    @Query("SELECT id from Details WHERE user.id = ?1")
    Long getIdForUserId(Long uid);

    @Query("SELECT budget from Details WHERE id = ?1")
    BigDecimal getBudgetForId(Long id);

    @Query("SELECT savings from Details WHERE id = ?1")
    BigDecimal getSavingsForId(Long id);

    @Query("SELECT spent from Details WHERE id = ?1")
    BigDecimal getSpentForId(Long id);
}
