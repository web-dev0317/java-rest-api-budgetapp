package io.berbotworks.budgetapp.services;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import io.berbotworks.budgetapp.exceptions.ExpenseNotFoundException;
import io.berbotworks.budgetapp.models.Details;
import io.berbotworks.budgetapp.models.Expense;
import io.berbotworks.budgetapp.models.History;
import io.berbotworks.budgetapp.models.TableEntry;
import io.berbotworks.budgetapp.models.User;
import io.berbotworks.budgetapp.repositories.DetailsRepository;
import io.berbotworks.budgetapp.repositories.ExpenseRepository;
import io.berbotworks.budgetapp.repositories.HistoryRepository;
import io.berbotworks.budgetapp.repositories.TableEntryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DetailsService {

    private final DetailsRepository detailsRepository;
    private final ExpenseRepository expenseRepository;
    private final HistoryRepository historyRepository;
    private final TableEntryRepository tableEntryRepository;

    public Details getDetails(Long uid) {
        return detailsRepository.findByUser_Id(uid).get();
    }

    // RESET, SET, ADD, DED => type DONE
    public String updateNumbers(Long uid, BigDecimal amount, String type) {
        Long detailsId = getDetailsId(uid);
        BigDecimal savings = detailsRepository.getSavingsForId(detailsId);
        BigDecimal budget = detailsRepository.getBudgetForId(detailsId);

        switch (type) {
            case "SET":
                // can additionally check whether budget > 0
                detailsRepository.updateBudgetById(amount, detailsId);
                detailsRepository.updateSavingsById(amount, detailsId);
                break;
            case "RESET":
                // can additionally check whether budget > 0
                detailsRepository.updateBudgetById(amount, detailsId);
                detailsRepository.updateSavingsById(amount.add(savings), detailsId);
                detailsRepository.updateSpentById(BigDecimal.ZERO, detailsId);
                break;
            case "ADD":
                detailsRepository.updateBudgetById(budget.add(amount), detailsId);
                detailsRepository.updateSavingsById(savings.add(amount), detailsId);
                break;
            case "DED":
                // can additionally check whether budget > 0
                detailsRepository.updateBudgetById(budget.subtract(amount), detailsId);
                detailsRepository.updateSavingsById(savings.subtract(amount), detailsId);
                break;
        }
        // can send updated numbers
        return "Updated numbers";
    }

    public User detailsForNewUser(User user) {
        BigDecimal zero = BigDecimal.ZERO;
        Details details = Details.builder()
                .budget(zero).savings(zero).spent(zero).user(user).build();
        // user.setDetails(detailsRepository.save(details));
        // since cascading
        user.setDetails(details);
        return user;
    }

    public Set<Expense> getExpenses(Long uid) {
        return expenseRepository
                .findAllByDetails_Id(getDetailsId(uid));
    }

    public Set<TableEntry> getTableEntries(Long uid) {
        return tableEntryRepository
                .findAllByDetails_Id(getDetailsId(uid));
    }

    public Set<Expense> getExpensesForDate(Long uid, String date) {
        return expenseRepository.findAllByDateAndDetails_Id(date, getDetailsId(uid));
    }

    public Expense newExpense(Long uid, Expense expense) {
        Details details = detailsRepository.findByUser_Id(uid).get();
        expense.setDetails(details);
        expense = expenseRepository.save(expense);

        Long detailsId = details.getId();
        BigDecimal price = expense.getPrice();
        BigDecimal newSpent = details.getSpent().add(price);
        BigDecimal newSavings = details.getSavings().subtract(price);

        detailsRepository.updateSavingsById(newSavings, detailsId);
        detailsRepository.updateSpentById(newSpent, detailsId);

        return expense;
    }

    public Expense deleteExpense(Long uid, Long expenseId) {
        Optional<Expense> e = expenseRepository.findById(expenseId);

        if (!e.isPresent()) {
            throw new ExpenseNotFoundException(new StringBuilder()
                    .append("Expense with id ")
                    .append(expenseId)
                    .append(" does not exist")
                    .toString());
        }
        expenseRepository.deleteById(expenseId);

        Long detailsId = getDetailsId(uid);
        BigDecimal price = e.get().getPrice();
        BigDecimal newSpent = detailsRepository
                .getSpentForId(detailsId).subtract(price);
        BigDecimal newSavings = detailsRepository
                .getSavingsForId(detailsId).add(price);

        detailsRepository.updateSavingsById(newSavings, detailsId);
        detailsRepository.updateSpentById(newSpent, detailsId);

        return e.get();
    }

    public History addToHistory(Long uid, History history) {
        Details details = detailsRepository.findByUser_Id(uid).get();
        history.setDetails(details);
        return historyRepository.save(history);
    }

    public TableEntry updateEntry(Long uid, TableEntry tableEntry) {
        Long entryId = tableEntry.getId();
        Optional<TableEntry> te = tableEntryRepository.findById(entryId);

        if (te.isPresent()) {
            // have to check if tableEntry.getSpent() is 0
            // and if its 0, delete entry
            // have to generate date
            tableEntryRepository
                    .updateSpentById(tableEntry.getSpent(), entryId);
        } else {
            Details details = detailsRepository.findByUser_Id(uid).get();
            tableEntry.setDetails(details);
            tableEntryRepository.save(tableEntry);
        }
        // can send updated object
        return tableEntry;
    }

    public Long getDetailsId(Long uid) {
        return detailsRepository.getIdForUserId(uid);
    }
}
