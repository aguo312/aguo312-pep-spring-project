package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) throws DuplicateUsernameException {
        if (accountRepository.getAccountByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }
        else if (account.getUsername().length() > 0 && account.getPassword().length() >= 4) {
            return accountRepository.save(account);
        }
        return null;
    }

    public Account login(Account account) {
        return accountRepository.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword()).orElse(null);
    }

}
