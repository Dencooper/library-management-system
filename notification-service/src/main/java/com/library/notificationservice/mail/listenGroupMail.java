package com.library.notificationservice.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.commonservice.dto.response.BorrowingRequestResponse;
import com.library.commonservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class listenGroupMail {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

//    @RetryableTopic(
//            attempts = "4",
//            backoff = @Backoff(delay = 1000, multiplier = 2),
//            autoCreateTopics = "true",
//            dltStrategy = DltStrategy.FAIL_ON_ERROR,
//            include = {RetryException.class, RuntimeException.class}
//    )
//    @KafkaListener(topics = "acceptBorrowingRequestMail", containerFactory = "kafkaListenerContainerFactory")
//    public void listen(String message) {
//        System.out.println("receiving message " +message);
//        throw new RuntimeException("Error test");
//    }
//
//    @DltHandler
//    void handleDtlMessage(@Payload String message){
//        System.out.println("DTL receiving message: " + message);
//    }
    
    @KafkaListener(id = "notificationGroup", topics = "notification")
    public void listen(String message) throws JsonProcessingException {
        BorrowingRequestResponse payload = objectMapper.readValue(message, BorrowingRequestResponse.class);
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("studentName", payload.getUser().getFullName());
        placeholders.put("borrowedBooks", payload.getBooks());
        placeholders.put("librarianName", payload.getLibrarian().getFullName());
        placeholders.put("librarianEmail", payload.getLibrarian().getEmail());
        emailService.sendEmailWithTemplate(payload.getUser().getEmail(), "Accept Request Borrowing ID -  " + payload.getId(), "acceptBorrowingRequestTemplate.ftl", placeholders, null);
    }

    @KafkaListener(id = "notificationGroup1", topics = "notificationAccept")
    public void listen1(BorrowingRequestResponse payload) {
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("name", payload.getUser().getFullName());
        emailService.sendEmailWithTemplate(payload.getUser().getEmail(), "Accept Request Borrowing", "acceptBorrowingRequestTemplate.ftl", placeholders, null);
    }
}
