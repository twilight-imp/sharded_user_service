package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Event;
import org.example.entity.User;
import org.example.entity.UserEvent;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserConsumer {

    @Value("${kafka.topic}")
    private String consumeTopic;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @KafkaListener(topics = "${kafka.topic}")
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("Получено сообщение из topic-event: {}", message);
        UserEvent userEvent = objectMapper.readValue(message, UserEvent.class);
        if (userEvent.getEvent().equals(Event.CREATED)) {
            User user = new User(userEvent.getId(), userEvent.getName(), userEvent.getEmail());
            userRepository.saveUser(user);
            log.info("Запись добавлена в базу: ID={}", user.getId());
        } else if (userEvent.getEvent().equals(Event.UPDATED)) {
            Optional<User> user = userRepository.getUser(userEvent.getId());
            if (user.isPresent()) {
                userRepository.updateUser(user.get());
                log.info("Запись обновлена в базе: ID={}", user.get().getId());
            } else {
                log.warn("Пользователь с ID {} не найден в базе!", user.get().getId());
            }
        }
    }
}