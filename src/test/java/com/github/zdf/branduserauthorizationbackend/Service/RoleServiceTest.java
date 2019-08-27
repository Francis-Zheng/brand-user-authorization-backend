package com.github.zdf.branduserauthorizationbackend.Service;

import com.github.zdf.branduserauthorizationbackend.BaseTest;
import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.security.Constants;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RoleServiceTest extends BaseTest {
    @Autowired
    private RoleService roleService;

    @Test
    public void insertTest() {
        List<Role> roleList = new ArrayList<>();

        Role role = new Role();
        role.setRoleId("111");
        role.setRoleName(Constants.ROLE_SUPER_ADMIN);

        Role role1 = new Role();
        role1.setRoleId("222");
        role1.setRoleName(Constants.ROLE_GOVERNMENT_USER);

        Role role2 = new Role();
        role2.setRoleId("333");
        role2.setRoleName(Constants.ROLE_COMPANY_USER);

        Role role3 = new Role();
        role3.setRoleId("444");
        role3.setRoleName(Constants.ROLE_EXPERT);

        roleList.add(role);
        roleList.add(role1);
        roleList.add(role2);
        roleList.add(role3);
        roleService.saveAll(roleList);
        Assert.assertNotNull(roleService.getAll());
    }

    @Test
    public void deleteTest() {
        roleService.deleteById("111");
        roleService.deleteById("222");
        roleService.deleteById("333");
        roleService.deleteById("444");
        Assert.assertNull(roleService.getById("111").orElse(null));
        Assert.assertNull(roleService.getById("222").orElse(null));
        Assert.assertNull(roleService.getById("333").orElse(null));
        Assert.assertNull(roleService.getById("444").orElse(null));
    }
}
