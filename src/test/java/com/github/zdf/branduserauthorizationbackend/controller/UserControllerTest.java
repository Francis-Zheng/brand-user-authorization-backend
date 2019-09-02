package com.github.zdf.branduserauthorizationbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.zdf.branduserauthorizationbackend.BaseTest;
import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.security.Constants;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

public class UserControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertAndDeleteTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        /*
         * 在mockmvc中，若传输user对象会进行一次序列化，会导致用户设置的密码变成空值，
         * 在保存的时候就会产生NullPointerException异常。
         * （因为在实体类中对password自定义序列化）
         */
        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "111");
        root1.put("username", "zheng");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(root1)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    public void getAllTest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "111");
        root1.put("username", "zheng");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andDo(print())

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    public void partialUpdateTest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "111");
        root1.put("username", "zheng");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        User userUpdate = new User();
        userUpdate.setBrandName("meidijituan");


        mockMvc.perform(MockMvcRequestBuilders.patch("/user/zheng").contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(root1)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    User updatedUser = mapper.readValue(json, User.class);
                    Assert.assertEquals("meidijituan", updatedUser.getBrandName());

                });

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void partialUpdateTest2() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "111");
        root1.put("username", "zheng");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        ObjectNode role1 = mapper.createObjectNode();
        role1.put("roleId", "111");
        role1.put("roleName", "admin");

        ArrayNode roles = mapper.createArrayNode();
        roles.add(role1);

        root1.put("roles", roles);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());


        mockMvc.perform(MockMvcRequestBuilders.get("/user/get/zheng"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(mvcResult ->{
                    User userOld = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), User.class);
                    User userNew = mapper.readValue(root1.toString(), User.class);
                    System.out.println("userOld: " + userOld.toString());
                    System.out.println("userNew: " + userNew.toString());
                    userOld.setRoles(userNew.getRoles());
                    mockMvc.perform(MockMvcRequestBuilders.patch("/user/zheng").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(userOld)))
                            .andDo(MockMvcResultHandlers.print())
                            .andExpect(MockMvcResultMatchers.status().isOk());
                });

        User userUpdate = new User();
        userUpdate.setBrandName("meidijituan");


        mockMvc.perform(MockMvcRequestBuilders.patch("/user/zheng").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(userUpdate)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    User updatedUser = mapper.readValue(json, User.class);
                    Assert.assertEquals("meidijituan", updatedUser.getBrandName());

                });

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void changePasswordTest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "333");
        root1.put("username", "haha");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        ObjectNode role1 = mapper.createObjectNode();
        role1.put("roleId", "111");
        role1.put("roleName", "ROLE_ZHENGDONGFA");

        ObjectNode role2 = mapper.createObjectNode();
        role2.put("roleId", "222");
        role2.put("roleName", "ROLE_ADMIN");

        ArrayNode roles = mapper.createArrayNode();
        roles.add(role1);
        roles.add(role2);

        root1.put("roles", roles);

        System.out.println(root1.toString());

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/password/haha")
                .param("username", "").param("oldPassword", "123").param("newPassword", "123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertTrue(passwordEncoder.matches("123456", userService.getByUserName("haha").getPassword()));
        Assert.assertFalse(passwordEncoder.matches("123", userService.getByUserName("haha").getPassword()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/333"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
