package com.example.demo.controller;

import com.example.demo.service.LoyaltyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loyaltyUser")
public class LoyaltyUserController {
    @Autowired
    private LoyaltyUserService loyaltyUserService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getLoyalPointByUsername(@PathVariable String username){
        int point = loyaltyUserService.getLoyaltyPointByUsername(username);
        return ResponseEntity.ok(point);
    }
}
