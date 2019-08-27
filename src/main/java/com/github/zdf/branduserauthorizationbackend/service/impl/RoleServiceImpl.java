package com.github.zdf.branduserauthorizationbackend.service.impl;

import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.repository.RoleRepository;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import org.springframework.stereotype.Service;

@Service    //在实现类中才添加@Service注解，BaseServiceImpl因为是基类所以不用注解
public class RoleServiceImpl extends BaseServiceImpl<Role, String> implements RoleService {
    private RoleRepository roleRepository;

    protected RoleServiceImpl(RoleRepository repository) {
        super(repository);
        this.roleRepository = repository;
    }

    @Override
    public String getId(Role entity) {
        return entity.getRoleId();
    }

    @Override
    public Role getByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public void deleteByRoleName(String roleName) {
        roleRepository.deleteByRoleName(roleName);
    }
}
