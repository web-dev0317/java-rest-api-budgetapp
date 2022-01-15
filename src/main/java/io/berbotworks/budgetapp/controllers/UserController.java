package io.berbotworks.budgetapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.berbotworks.budgetapp.dto.BudgetAppResponse;

@RestController
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<?> newUser() {
        BudgetAppResponse budgetAppResponse = BudgetAppResponse.builder()
                .message("User created")
                .success(true)
                .build();
        return new ResponseEntity<>(budgetAppResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn() {
        return ResponseEntity.noContent().build();
    }
}
