package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageByID(int messageID) {
        return messageRepository.findByMessageId(messageID);
    }

    public int deleteMessageByID(int messageID) {
        if (messageRepository.existsById(messageID)) {
            messageRepository.deleteById(messageID);

            return 1;
        }

        return 0;
    }

    public Integer updateMessage(int messageID, String messageText) {
        Message message = messageRepository.findByMessageId(messageID);

        if (message != null) {
            message.setMessageText(messageText);

            messageRepository.save(message);

            return 1;
        }

        return 0;
    }

    public List<Message> getMessagesByAccountID(int accoundID) {
        return messageRepository.findAllByPostedBy(accoundID);
    }
}