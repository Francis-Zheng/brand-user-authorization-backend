package com.github.zdf.branduserauthorizationbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zdf.branduserauthorizationbackend.BaseTest;
import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.security.Constants;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebAppConfiguration
public class RoleControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

    @Autowired
    private WebApplicationContext wac;

    //clientId
    private final static String CLIENT_ID = "Questionnaire_client";
    //clientSecret
    private final static String CLIENT_SECRET = "123456";
    //用户名
    private final static String USERNAME = "admin";
    //密码
    private final static String PASSWORD = "123456";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())    //配置方法：https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#ns-web-xml，9.2.1 Setting Up MockMvc and Spring Security
                .build();//初始化MockMvc对象,添加Security过滤器链
    }

    public String obtainAccessToken() throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("username", USERNAME);
        params.add("password", PASSWORD);

        // @formatter:off

        ResultActions result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));

        // @formatter:on

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
//        System.out.println(jsonParser.parseMap(resultString).get("access_token").toString());
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    public void getAccessToken() throws Exception {
        final String accessToken = obtainAccessToken();
        Assert.assertNotNull(accessToken);
    }

    @Test
    public void getByRoleIdTest() throws Exception{
        final String accessToken = obtainAccessToken();

        Role role = new Role();
        role.setRoleId("111");
        role.setRoleName(Constants.ROLE_SUPER_ADMIN);
        roleService.saveUnique(role);
        mockMvc.perform(MockMvcRequestBuilders.get("/role/111").param("access_token", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
        roleService.deleteById("111");
        Assert.assertNull(roleService.getById("111").orElse(null));
    }

    @Test
    public void getAllTest() throws Exception{
        final String accessToken = obtainAccessToken();

        Role role = new Role();
        role.setRoleId("111");
        role.setRoleName(Constants.ROLE_SUPER_ADMIN);
        roleService.saveUnique(role);

        mockMvc.perform(MockMvcRequestBuilders.get("/role").param("access_token", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andDo(print())
        roleService.deleteById("111");
        Assert.assertNull(roleService.getById("111").orElse(null));

    }

    @Test
    public void getByRoleNameTest() throws Exception{
        final String accessToken = obtainAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/role/get/COMPANY").param("access_token", accessToken))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteByRoleNameTest() throws Exception{
        final String accessToken = obtainAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.delete("/role/delete/COMPANY").param("access_token", accessToken))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void insertTest() throws Exception{
        final String accessToken = obtainAccessToken();

        ObjectMapper mapper = new ObjectMapper();
        Role testRole = new Role();
        testRole.setRoleName("master");
        testRole.setRoleId("2018");
        mockMvc.perform(MockMvcRequestBuilders.post("/role").param("access_token", accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(testRole))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.delete("/role/2018").param("access_token", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
