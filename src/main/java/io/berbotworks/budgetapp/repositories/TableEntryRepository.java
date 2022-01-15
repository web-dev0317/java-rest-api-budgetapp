package io.berbotworks.budgetapp.repositories;

import java.math.BigDecimal;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import io.berbotworks.budgetapp.models.TableEntry;

public interface TableEntryRepository extends CrudRepository<TableEntry, Long> {

    Set<TableEntry> findAllByDetails_Id(Long detailsId);

    @Transactional
    @Modifying
    @Query("UPDATE TableEntry te SET te.spent = ?1 WHERE te.id = ?2")
    void updateSpentById(BigDecimal spent, Long id);
}
