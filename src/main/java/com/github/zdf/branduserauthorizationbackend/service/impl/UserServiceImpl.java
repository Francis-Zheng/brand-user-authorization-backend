package com.github.zdf.branduserauthorizationbackend.service.impl;

import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.repository.UserRepository;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {
    private UserRepository userRepository;

    protected UserServiceImpl(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    @Override
    public User getByUserName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null)
            return null;
        user.setPassword(null);
        return user;
    }

    @Override
    public List<User> getByBrandName(String brandName) {
        List<User> users = userRepository.findByBrandName(brandName);
        if (users.isEmpty())
            return null;
        users.parallelStream().forEach(user -> user.setPassword(null));
        return users;
    }

    @Override
    public void deleteByUserName(String userName) {
        userRepository.deleteByUsername(userName);
    }

    @Override
    public void deleteByBrandName(String brandName) {
        userRepository.deleteByBrandName(brandName);
    }

    @Override
    public String getId(User entity) {
        return entity.getUserId();
    }

    @Override
    public Optional<User> getById(String s) {
        Optional<User> users = userRepository.findById(s);
        users.ifPresent(user -> user.setPassword(null));
        return users;
    }

    @Override
    public Iterable<User> getAllByExample(User example) {
        Iterable<User> users = userRepository.findAll(Example.of(example));
        if (users.iterator().hasNext()) {
            users.forEach(user -> user.setPassword(null));
            return users;
        } else return null;
    }

    @Override
    public Iterable<User> getAll() {
        Iterable<User> users = userRepository.findAll();
        if (users.iterator().hasNext()) {
            users.forEach(user -> user.setPassword(null));
            return users;
        } else return null;
    }

    @Override
    public Iterable<User> getAllById(Iterable<String> strings) {
        Iterable<User> users = userRepository.findAllById(strings);
        if (users.iterator().hasNext()) {
            users.forEach(user -> user.setPassword(null));
            return users;
        } else return null;
    }
}
