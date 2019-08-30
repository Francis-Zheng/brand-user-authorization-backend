package com.github.zdf.branduserauthorizationbackend.repository;

import com.github.zdf.branduserauthorizationbackend.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRoleName(String roleName);

    void deleteByRoleName(String roleName);

    boolean existsRoleByRoleName(String roleName);
}
