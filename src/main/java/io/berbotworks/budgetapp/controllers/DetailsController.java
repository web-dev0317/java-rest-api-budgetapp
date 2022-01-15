package io.berbotworks.budgetapp.controllers;

import javax.servlet.ServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.berbotworks.budgetapp.controllers.responses.StandardResponses;
import io.berbotworks.budgetapp.dto.BudgetAppRequest;
import io.berbotworks.budgetapp.dto.BudgetAppResponse;
import io.berbotworks.budgetapp.exceptions.ExpenseNotFoundException;
import io.berbotworks.budgetapp.services.DetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DetailsController {
    private final DetailsService detailsService;

    @GetMapping("/details")
    public ResponseEntity<?> getDetails(ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        log.debug("uid from request obj : {}", uid);
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.getDetails(uid))
                .build();

        return StandardResponses.okResponse(budgetAppResponse);
    }

    @PutMapping("/numbers")
    public ResponseEntity<?> updateNumbers(@RequestBody BudgetAppRequest request, ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.updateNumbers(uid, request.getAmount(), request.getType()))
                .build();
        return StandardResponses.okResponse(budgetAppResponse);
    }

    @GetMapping("/expenses")
    public ResponseEntity<?> getExpenses(ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.getExpenses(uid)).build();
        return StandardResponses.okResponse(budgetAppResponse);
    }

    @GetMapping("/expenses/{date}")
    public ResponseEntity<?> getExpensesForDate(@PathVariable String date, ServletRequest servletRequest) {
        log.debug("Date : {}", date);
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.getExpensesForDate(uid, date)).build();
        return StandardResponses.okResponse(budgetAppResponse);
    }

    @PostMapping("/expense")
    public ResponseEntity<?> newExpense(@RequestBody BudgetAppRequest request, ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.newExpense(uid, request.getExpense()))
                .success(true)
                .message("Expense created")
                .build();
        return new ResponseEntity<>(budgetAppResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/expense")
    public ResponseEntity<?> deleteExpense(@RequestBody BudgetAppRequest request, ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.deleteExpense(uid, request.getExpense().getId()))
                .build();
        return StandardResponses.okResponse(budgetAppResponse);
    }

    @GetMapping("/tablentries")
    public ResponseEntity<?> getTableEntries(ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.getTableEntries(uid))
                .build();
        return StandardResponses.okResponse(budgetAppResponse);
    }

    @PutMapping("/tablentry")
    public ResponseEntity<?> updateTableEntries(@RequestBody BudgetAppRequest request, ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.updateEntry(uid, request.getTableEntry()))
                .build();
        return StandardResponses.okResponse(budgetAppResponse);
    }

    @PostMapping("/history")
    public ResponseEntity<?> addToHistory(@RequestBody BudgetAppRequest request, ServletRequest servletRequest) {
        Long uid = ((Integer) servletRequest.getAttribute("uid")).longValue();
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .data(detailsService.addToHistory(uid, request.getHistory()))
                .build();
        return StandardResponses.okResponse(budgetAppResponse);
    }

    // @ExceptionHandler - controller level annotation
    // to handle exceptions within controller
    // @ControllerAdvice - class level
    // to handle exceptions globally (exceptions from all controllers)
    // also see @ResponseStatus
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<?> handleExpenseNotFoundException(Exception e) {
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(budgetAppResponse, HttpStatus.BAD_REQUEST);
    }

}
