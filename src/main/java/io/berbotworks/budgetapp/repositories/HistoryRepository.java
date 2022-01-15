package io.berbotworks.budgetapp.repositories;

import org.springframework.data.repository.CrudRepository;

import io.berbotworks.budgetapp.models.History;

public interface HistoryRepository extends CrudRepository<History, Long> {

}
