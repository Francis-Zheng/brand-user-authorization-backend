package com.github.zdf.branduserauthorizationbackend.service;

import com.github.zdf.branduserauthorizationbackend.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleService extends BaseService<Role, String> {
    /**
     * 根据角色名查找角色
     * @param roleName 要查询的角色名
     * @return 查询到的角色
     */
    Role getByRoleName(String roleName);

    /**
     * 根据角色名删除角色
     * @param roleName 要删除的角色名
     */
    void deleteByRoleName(String roleName);
}
