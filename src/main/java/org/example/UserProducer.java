package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.entity.UserEvent;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserProducer {
    @Value("${kafka.topic}")
    private String produceTopic;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String send(UserEvent userEvent) {
        try {
            String message = objectMapper.writeValueAsString(userEvent);
            log.info("Отправка сообщения в topic: {}", message);
            kafkaTemplate.send(produceTopic, userEvent.getId().toString(), message);
            return "Отправлено событие для пользователя с id: " + userEvent.getId();
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализации объекта UserEvent в JSON", e);
            return "Error parse json";
        }
    }

}