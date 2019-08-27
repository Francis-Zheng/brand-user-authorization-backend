package com.github.zdf.branduserauthorizationbackend.Service;

import com.github.zdf.branduserauthorizationbackend.BaseTest;
import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Test
    public void insertTest() {
        User user = new User();
        user.setUserName("zdf");
        user.setUserId("111");
        user.setPassword("111");
        user.setBrandName("meidi");
        Set<Role> roleSet = new HashSet<>();
        roleService.getAll().forEach(roleSet::add);
        user.setRoles(roleSet);
        userService.save(user);

        User user1 = new User();
        user1.setUserName("admin");
        user1.setUserId("222");
        user1.setPassword("222");
        user1.setBrandName("haier");
        Set<Role> roleSet1 = new HashSet<>();
        roleService.getAll().forEach(roleSet1::add);
        user1.setRoles(roleSet1);
        userService.save(user1);

//        System.out.println(userService.getAll().toString());
        Assert.assertNotNull(userService.getAll());

    }

    @Test
    public void getByUserNameTest() {
        System.out.println(userService.getByUserName("zdf").toString());
        Assert.assertNotNull(userService.getByUserName("zdf"));
    }

    @Test
    public void deleteTest() {
        userService.deleteById("111");
        userService.deleteById("222");
        Assert.assertNull(userService.getById("111").orElse(null));
        Assert.assertNull(userService.getById("222").orElse(null));
    }
}
