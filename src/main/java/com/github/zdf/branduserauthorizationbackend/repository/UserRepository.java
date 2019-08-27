package com.github.zdf.branduserauthorizationbackend.repository;

import com.github.zdf.branduserauthorizationbackend.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(String userName);

    List<User> findByBrandName(String brandName);

    void deleteByUserName(String userName);

    void deleteByBrandName(String brandName);
}
