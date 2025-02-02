package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if (message.getMessageText().length() > 0 && message.getMessageText().length() <= 255 && accountRepository.findById(message.getPostedBy()).isPresent()) {
            return messageRepository.save(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public int deleteMessageById(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    public int patchMessageById(Message message, int messageId) {
        if (messageRepository.existsById(messageId) && message.getMessageText().length() > 0 && message.getMessageText().length() <= 255) {
            Message oldMessage = messageRepository.findById(messageId).get();
            oldMessage.setMessageText(message.getMessageText());
            messageRepository.save(oldMessage);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessagesFromUserByAccountId(int accountId) {
        return messageRepository.getMessagesByPostedBy(accountId);
    }
}
