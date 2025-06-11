package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.UserProducer;
import org.example.dto.UserDto;
import org.example.entity.Event;
import org.example.entity.User;
import org.example.entity.UserEvent;
import org.example.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final UserProducer userProducer;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userProducer = userProducer;
    }


    @Transactional
    public String createUser(UserDto userDto) {
        log.info("Создание пользователя", userDto.getId());
        if (userDto.getId() == null) {
            userDto.setId(UUID.randomUUID());
        }
        UserEvent userEvent = modelMapper.map(userDto, UserEvent.class);
        userEvent.setEvent(Event.CREATED);

        return userProducer.send(userEvent);
    }

    public UserDto getUser(UUID id) {
        Optional<User> user = userRepository.getUser(id);
        if (user.isEmpty()) {
            throw new RuntimeException("Пользователь с id " + id + " не найден");
        }
        return modelMapper.map(user, UserDto.class);
    }
    @Transactional
    public String updateUser(UserDto userDto) {
        log.info("Обновление пользователя с ID: {}", userDto.getId());

        Optional<User> optionalUser = userRepository.getUser(userDto.getId());
        System.out.println(optionalUser.isEmpty());
        if (optionalUser.isEmpty()) {
            log.error("Пользователь с ID {} не найден", userDto.getId());
            throw new RuntimeException("Пользователь с ID " + userDto.getId() + " не найден");
        }
        User user = optionalUser.get();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        userRepository.updateUser(user);

        UserEvent userEvent = modelMapper.map(userDto, UserEvent.class);
        userEvent.setEvent(Event.UPDATED);
        return userProducer.send(userEvent);
    }
}