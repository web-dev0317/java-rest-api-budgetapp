package io.berbotworks.budgetapp.dto;

import java.math.BigDecimal;

import io.berbotworks.budgetapp.models.Expense;
import io.berbotworks.budgetapp.models.History;
import io.berbotworks.budgetapp.models.TableEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BudgetAppRequest {
    private BigDecimal amount;
    private String type;
    private Expense expense;
    private TableEntry tableEntry;
    private History history;
}
