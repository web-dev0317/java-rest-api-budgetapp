package io.berbotworks.budgetapp.controllers.responses;

import org.springframework.http.ResponseEntity;

import io.berbotworks.budgetapp.dto.BudgetAppResponse;

public class StandardResponses {

    public static ResponseEntity<?> okResponse(BudgetAppResponse budgetAppResponse) {
        budgetAppResponse.setSuccess(true);
        return ResponseEntity.ok(budgetAppResponse);
    }
}
