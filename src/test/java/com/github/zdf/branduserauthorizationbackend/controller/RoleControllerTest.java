package com.github.zdf.branduserauthorizationbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zdf.branduserauthorizationbackend.BaseTest;
import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.security.Constants;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class RoleControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

    @Test
    public void getByRoleIdTest() throws Exception{
        Role role = new Role();
        role.setRoleId("111");
        role.setRoleName(Constants.ROLE_SUPER_ADMIN);
        roleService.saveUnique(role);
        mockMvc.perform(MockMvcRequestBuilders.get("/role/111"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        roleService.deleteById("111");
        Assert.assertNull(roleService.getById("111").orElse(null));
    }

    @Test
    public void getAllTest() throws Exception{
        Role role = new Role();
        role.setRoleId("111");
        role.setRoleName(Constants.ROLE_SUPER_ADMIN);
        roleService.saveUnique(role);

        mockMvc.perform(MockMvcRequestBuilders.get("/role"))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andDo(print())
        roleService.deleteById("111");
        Assert.assertNull(roleService.getById("111").orElse(null));

    }

    @Test
    public void getByRoleNameTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/role/get/COMPANY"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteByRoleNameTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/role/delete/COMPANY"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void insertTest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Role testRole = new Role();
        testRole.setRoleName("master");
        testRole.setRoleId("2018");
        mockMvc.perform(MockMvcRequestBuilders.post("/role")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(testRole))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.delete("/role/2018"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
