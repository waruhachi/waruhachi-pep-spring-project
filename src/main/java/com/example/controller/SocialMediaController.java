package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<Account> postRegister(@RequestBody Account account){
        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || username.isBlank() || password == null || password.length() < 4) {
            return ResponseEntity.status(400).body(null);
        }

        if (accountService.usernameExists(username)) {
            return ResponseEntity.status(409).body(null);
        }

        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> postLogin(@RequestBody Account account){
        String username = account.getUsername();
        String password = account.getPassword();

        Account existingAccount = accountService.login(username, password);

        if (existingAccount == null) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.ok(existingAccount);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> postMessages(@RequestBody Message message){
        int messagePostedBy = message.getPostedBy();
        String messageText = message.getMessageText();

        if (messageText == null || messageText.isBlank() || messageText.length() > 255 || !accountService.accountExists(messagePostedBy)) {
            return ResponseEntity.status(400).body(null);
        }

        Message createdMessage = messageService.createMessage(message);

        return ResponseEntity.ok(createdMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(){
        List<Message> messages = messageService.getAllMessages();

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageID}")
    public ResponseEntity<Message> getMessages(@PathVariable int messageID){
        Message message = messageService.getMessageByID(messageID);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{messageID}")
    public ResponseEntity<Integer> deleteMessages(@PathVariable int messageID){
        int rowsDeleted = messageService.deleteMessageByID(messageID);

        if (rowsDeleted == 0) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(rowsDeleted);
    }

    @PatchMapping("/messages/{messageID}")
    public ResponseEntity<Integer> patchMessages(@PathVariable int messageID, @RequestBody Message newMessageText){
        String messageText = newMessageText.getMessageText();

        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {   
            return ResponseEntity.status(400).body(0);
        }

        int rowsUpdated = messageService.updateMessage(messageID, messageText);

        if (rowsUpdated == 0) {
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok(rowsUpdated);
    }

    @GetMapping("/accounts/{accountID}/messages")
    public ResponseEntity<List<Message>> getAccountMessages(@PathVariable int accountID){
        List<Message> messages = messageService.getMessagesByAccountID(accountID);

        return ResponseEntity.ok(messages);
    }
}
