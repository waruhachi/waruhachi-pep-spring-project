package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean usernameExists(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean accountExists(int accoundID) {
        return accountRepository.existsById(accoundID);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        Account account = accountRepository.findByUsername(username);

        if (account != null && account.getPassword().equals(password)) {
            return account;
        }

        return null;
    }
}