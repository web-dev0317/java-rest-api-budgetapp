package io.berbotworks.budgetapp.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "user" })
@Entity
public class Details extends BaseEntity {

    private BigDecimal budget;
    private BigDecimal savings;
    private BigDecimal spent;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "details")
    private Set<Expense> expenses = new HashSet<>();

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "details")
    private Set<TableEntry> tableEntries = new HashSet<>();;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "details")
    private Set<History> historySet = new HashSet<>();

    @JsonBackReference
    @OneToOne
    private User user;

    @Builder
    public Details(Long id, BigDecimal budget, BigDecimal savings, BigDecimal spent, Set<Expense> expenses,
            Set<TableEntry> tableEntries, Set<History> historySet, User user) {
        super(id);
        this.budget = budget;
        this.savings = savings;
        this.spent = spent;
        this.expenses = expenses;
        this.tableEntries = tableEntries;
        this.historySet = historySet;
        this.user = user;
    }

    public Details budget(BigDecimal budget) {
        setBudget(budget);
        return this;
    }

    public Details savings(BigDecimal savings) {
        setSavings(savings);
        return this;
    }

    public Details spent(BigDecimal spent) {
        setSpent(spent);
        return this;
    }

    public Details expenses(Set<Expense> expenses) {
        setExpenses(expenses);
        return this;
    }

    public Details tableEntries(Set<TableEntry> tableEntries) {
        setTableEntries(tableEntries);
        return this;
    }

    public Details historySet(Set<History> historySet) {
        setHistorySet(historySet);
        return this;
    }

    public Details user(User user) {
        setUser(user);
        return this;
    }

}
