package com.example.demo.service;

import com.example.demo.repository.LoyaltyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyUserServiceImpl implements LoyaltyUserService {
    @Autowired
    private LoyaltyUserRepository loyaltyUserRepository;
    public int getLoyaltyPointByUsername(String username){
        return loyaltyUserRepository.findByUsername(username).getLoyaltyPoint();
    }
}
