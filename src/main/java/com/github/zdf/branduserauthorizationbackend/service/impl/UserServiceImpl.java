package com.github.zdf.branduserauthorizationbackend.service.impl;

import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.repository.UserRepository;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {
    private UserRepository userRepository;

    protected UserServiceImpl(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<User> getByBrandName(String brandName) {
        return userRepository.findByBrandName(brandName);
    }

    @Override
    public void deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }

    @Override
    public void deleteByBrandName(String brandName) {
        userRepository.deleteByBrandName(brandName);
    }

    @Override
    public String getId(User entity) {
        return entity.getUserId();
    }
}
