package com.github.zdf.branduserauthorizationbackend.Service;

import com.github.zdf.branduserauthorizationbackend.BaseTest;
import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.exception.EntityExistException;
import com.github.zdf.branduserauthorizationbackend.security.Constants;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void insertTest() {
        //先向数据库中插入角色
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


        User user = new User();
        user.setUsername("zdf");
        user.setUserId("111");
        user.setPassword("111");
        user.setBrandName("美的集团");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getByRoleName(Constants.ROLE_COMPANY_USER));
        user.setRoles(roleSet);
        userService.saveUnique(user);

        User user1 = new User();
        user1.setUsername("testName");
        user1.setUserId("222");
        user1.setPassword("222");
        user1.setBrandName("青岛海尔");
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(roleService.getByRoleName(Constants.ROLE_COMPANY_USER));
        user1.setRoles(roleSet1);
        userService.saveUnique(user1);

//        System.out.println(userService.getAll().toString());
        Assert.assertNotNull(userService.getAll());

        roleService.deleteById("111");
        roleService.deleteById("222");
        roleService.deleteById("333");
        roleService.deleteById("444");

        userService.deleteById("111");
        userService.deleteById("222");

    }

    @Test
    public void getByUserNameTest() {
        Role role = new Role();
        role.setRoleId("111");
        role.setRoleName(Constants.ROLE_COMPANY_USER);
        roleService.saveUnique(role);

        User user = new User();
        user.setUsername("haha");
        user.setUserId("111");
        user.setPassword("111");
        user.setBrandName("美的集团");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getByRoleName(Constants.ROLE_COMPANY_USER));
        user.setRoles(roleSet);
        userService.saveUnique(user);

        System.out.println(userService.getByUserName("haha").toString());
        Assert.assertNotNull(userService.getByUserName("haha"));
        Assert.assertNotNull(userService.getByBrandName("美的集团"));
        userService.deleteByUserName("haha");
        Assert.assertNull(userService.getByUserName("haha"));
        roleService.deleteById("111");
    }

    @Test
    public void getAllByExampleTest() {
        Role role = new Role();
        role.setRoleId("111");
        role.setRoleName(Constants.ROLE_COMPANY_USER);
        roleService.saveUnique(role);

        Role role1 = new Role();
        role1.setRoleId("222");
        role1.setRoleName(Constants.ROLE_EXPERT);
        roleService.saveUnique(role1);

        User user = new User();
        user.setUsername("tom");
        user.setUserId("111");
        user.setPassword("111");
        user.setBrandName("美的集团");
        Set<Role> roleSet = new HashSet<>();
        roleService.getAll().iterator().forEachRemaining(roleSet::add);
        user.setRoles(roleSet);
        userService.saveUnique(user);

        User user1 = new User();
        user1.setUsername("tom");
        System.out.println(userService.getByUserName("tom").toString());
        userService.getAllByExample(user1).iterator().next().getRoles().forEach(System.out::println);
        Assert.assertNotNull(userService.getAllByExample(user1));
        userService.deleteByUserName("tom");
        roleService.deleteById("111");
        roleService.deleteById("222");
    }

    @Test(expected = EntityExistException.class)
    public void uniqueSaveTest() throws EntityExistException {
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

        User user = new User();
        user.setUsername("zdf");
        user.setUserId("111");
        user.setPassword("111");
        user.setBrandName("美的集团");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getByRoleName(Constants.ROLE_COMPANY_USER));
        user.setRoles(roleSet);
        userService.saveUnique(user);

        User user1 = new User();
        user1.setUsername("zdf");
        user1.setUserId("222");
        user1.setPassword("111");
        user1.setBrandName("美的集团");
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(roleService.getByRoleName(Constants.ROLE_COMPANY_USER));
        user1.setRoles(roleSet1);
        try {
            userService.saveUnique(user1);
        } catch (EntityExistException e) {
            roleService.deleteById("111");
            roleService.deleteById("222");
            roleService.deleteById("333");
            roleService.deleteById("444");

            userService.deleteById("111");
            throw e;
        }
    }

    @Test
    public void partialUpdataTest() {
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

        User user = new User();
        user.setUsername("zdf");
        user.setUserId("111");
        user.setPassword("111");
        user.setBrandName("美的集团");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getByRoleName(Constants.ROLE_COMPANY_USER));
        user.setRoles(roleSet);
        userService.saveUnique(user);

        User user1 = new User();
        user1.setUsername("zdf");
        user1.setUserId("111");
        user1.setPassword("123");
        user1.setBrandName("meidi");
        userService.partialUpdate(user.getUsername(), user1);
        Assert.assertTrue(passwordEncoder.matches("111", userService.getByUserName("zdf").getPassword()));

        roleService.deleteById("111");
        roleService.deleteById("222");
        roleService.deleteById("333");
        roleService.deleteById("444");

        userService.deleteById("111");
    }

    @Test(expected = BadCredentialsException.class)
    public void changePasswordTest() {

        User user = new User();
        user.setUsername("zdf");
        user.setUserId("111");
        user.setPassword("111");
        user.setBrandName("美的集团");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getByRoleName(Constants.ROLE_COMPANY_USER));
        user.setRoles(roleSet);
        userService.saveUnique(user);

        userService.changeUserPassword("zdf", "111", "123456");
        Assert.assertFalse(passwordEncoder.matches("111", userService.getByUserName("zdf").getPassword()));
        Assert.assertTrue(passwordEncoder.matches("123456", userService.getByUserName("zdf").getPassword()));

        //修改密码的用户名为空，抛出异常
        try {
            userService.changeUserPassword("", "1", "123");
        } catch (BadCredentialsException e) {
            userService.deleteByUserName("zdf");
            throw e;
        }
    }

}
