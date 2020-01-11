package com.example.demo.repository;

import com.example.demo.entity.loyaltyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyUserRepository extends JpaRepository<loyaltyUser, String> {
    public loyaltyUser findByUsername(String username);
}
