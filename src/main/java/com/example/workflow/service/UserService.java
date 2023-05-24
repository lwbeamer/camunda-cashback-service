package com.example.workflow.service;

import com.example.workflow.exception.NotFoundUserException;
import com.example.workflow.model.User;
import com.example.workflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;



    public User getUser(String username) throws NotFoundUserException {
        User user = userRepository.findById(username).orElse(null);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundUserException("Пользователь с таким именем не найден!");
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


}
