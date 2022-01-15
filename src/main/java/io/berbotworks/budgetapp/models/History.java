package io.berbotworks.budgetapp.models;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "details" })
@Entity
public class History extends BaseEntity {

    private String monthAndYear;
    private BigDecimal budget;
    private BigDecimal savings;
    private BigDecimal spent;

    @JsonBackReference
    @ManyToOne
    private Details details;

    public History(String monthAndYear, BigDecimal budget, BigDecimal savings, BigDecimal spent) {
        this.monthAndYear = monthAndYear;
        this.budget = budget;
        this.savings = savings;
        this.spent = spent;
    }

}
