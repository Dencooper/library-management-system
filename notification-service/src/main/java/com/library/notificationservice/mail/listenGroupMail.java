package com.library.notificationservice.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.commonservice.dto.response.BorrowingResponse;
import com.library.commonservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
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

    @KafkaListener(id = "notificationGroup1", topics = "borrowingNotification")
    public void listenBorrowingNotification(String message) throws JsonProcessingException {
        BorrowingResponse payload = objectMapper.readValue(message, BorrowingResponse.class);
        Map<String, Object> placeholders = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        placeholders.put("studentName", payload.getUser().getFullName());
        placeholders.put("studentEmail", payload.getUser().getEmail());
        placeholders.put("studentPhone", payload.getUser().getPhone());
        placeholders.put("librarianName", payload.getLibrarian().getFullName());
        placeholders.put("librarianEmail", payload.getLibrarian().getEmail());
        placeholders.put("librarianPhone", payload.getLibrarian().getPhone());
        placeholders.put("borrowedAt", payload.getBorrowedAt().format(formatter));
        placeholders.put("dueDate", payload.getBorrowedAt().plusDays(30).format(formatter));
        placeholders.put("borrowedBooks", payload.getItems());
        emailService.sendEmailWithTemplate(payload.getUser().getEmail(), "Borrowing #" + payload.getId() + " - Online Library", "borrowingNotificationTemplate.ftl", placeholders, null);
    }

    @KafkaListener(id = "notificationGroup2", topics = "returnReminderNotification")
    public void listenReturnReminderNotification(String message) throws JsonProcessingException {
        BorrowingResponse payload = objectMapper.readValue(message, BorrowingResponse.class);
        Map<String, Object> placeholders = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        placeholders.put("studentName", payload.getUser().getFullName());
        placeholders.put("studentEmail", payload.getUser().getEmail());
        placeholders.put("studentPhone", payload.getUser().getPhone());
        placeholders.put("librarianName", payload.getLibrarian().getFullName());
        placeholders.put("librarianEmail", payload.getLibrarian().getEmail());
        placeholders.put("librarianPhone", payload.getLibrarian().getPhone());
        placeholders.put("borrowedAt", payload.getBorrowedAt().format(formatter));
        placeholders.put("dueDate", payload.getBorrowedAt().plusDays(30).format(formatter));
        placeholders.put("borrowedBooks", payload.getItems());
        emailService.sendEmailWithTemplate(payload.getUser().getEmail(), "Reminder Return Borrowing #" + payload.getId() + " - Online Library", "returnReminderNotificationTemplate.ftl", placeholders, null);
    }
}
