package com.example.demo.kafka;

import com.example.demo.dto.AnalyticsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class AnalyticsProducer {

    private static final String TOPIC = "analytics-events";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(AnalyticsEvent event) {
        kafkaTemplate.send(TOPIC, event);
        System.out.println("✅ Analytics event sent: " + event.getEventType()
                + " for " + event.getUsername());
    }
}