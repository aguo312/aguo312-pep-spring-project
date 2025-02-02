package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account newAccount = null;
        try {
            newAccount = accountService.register(account);
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(409).build();
        }
        if (newAccount == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account authorizedAccount = accountService.login(account);
        if (authorizedAccount != null) {
            return ResponseEntity.ok(authorizedAccount);
        }
        else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message newMessage = messageService.createMessage(message);
        if (newMessage != null) {
            return ResponseEntity.ok(newMessage);
        }
        else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message searchedMessage = messageService.getMessageById(messageId);
        if (searchedMessage != null) {
            return ResponseEntity.ok(searchedMessage);
        }
        else {
            return ResponseEntity.status(200).build();
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        if (messageService.deleteMessageById(messageId) == 1) {
            return ResponseEntity.ok(1);
        }
        else {
            return ResponseEntity.status(200).build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageById(@RequestBody Message message, @PathVariable int messageId) {
        if (messageService.patchMessageById(message, messageId) == 1) {
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesFromUser(@PathVariable int accountId) {
        return ResponseEntity.ok(messageService.getMessagesFromUserByAccountId(accountId));
    }
}
