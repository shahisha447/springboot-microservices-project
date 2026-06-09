package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnalyticsEvent {
    private String eventType;    // USER_REGISTERED, CUSTOMER_REGISTERED, USER_LOGIN, CUSTOMER_LOGIN
    private String username;
    private String source;       // demo
    private LocalDateTime timestamp;
    private String details;      // extra info

    // ✅ Easy builder method
    public static AnalyticsEvent of(String eventType, String username, String details) {
        AnalyticsEvent event = new AnalyticsEvent();
        event.setEventType(eventType);
        event.setUsername(username);
        event.setSource("demo");
        event.setTimestamp(LocalDateTime.now());
        event.setDetails(details);
        return event;
    }
}