package com.github.zdf.branduserauthorizationbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.zdf.branduserauthorizationbackend.BaseTest;
import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebAppConfiguration
public class UserControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public void insertAndDeleteTest() throws Exception {
        final String accessToken = obtainAccessToken();


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

        mockMvc.perform(post("/user").param("access_token", accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(root1)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111").header("authorization", "Bearer" + accessToken))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void getAllTest() throws Exception {
        final String accessToken = obtainAccessToken();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "111");
        root1.put("username", "zheng");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        mockMvc.perform(post("/user").param("access_token", accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.get("/user").param("access_token", accessToken))
                .andExpect(status().isOk());
//                .andDo(print())

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111").param("access_token", accessToken))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void partialUpdateTest() throws Exception {
        final String accessToken = obtainAccessToken();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "111");
        root1.put("username", "zheng");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        mockMvc.perform(post("/user").param("access_token", accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());

        User userUpdate = new User();
        userUpdate.setBrandName("meidijituan");


        mockMvc.perform(MockMvcRequestBuilders.patch("/user/zheng").param("access_token", accessToken).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(userUpdate)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    User updatedUser = mapper.readValue(json, User.class);
                    Assert.assertEquals("meidijituan", updatedUser.getBrandName());

                });

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111").param("access_token", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void partialUpdateTest2() throws Exception {
        final String accessToken = obtainAccessToken();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root1 = mapper.createObjectNode();
        root1.put("userId", "111");
        root1.put("username", "dong");
        root1.put("password", "123");
        root1.put("brandName", "美的集团");

        ObjectNode role1 = mapper.createObjectNode();
        role1.put("roleId", "111");
        role1.put("roleName", "admin");

        ArrayNode roles = mapper.createArrayNode();
        roles.add(role1);

        root1.put("roles", roles);

        mockMvc.perform(post("/user").param("access_token", accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());


        mockMvc.perform(MockMvcRequestBuilders.get("/user/get/dong").param("access_token", accessToken))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    User userOld = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), User.class);
                    User userNew = mapper.readValue(root1.toString(), User.class);
                    System.out.println("userOld: " + userOld.toString());
                    System.out.println("userNew: " + userNew.toString());
                    userOld.setRoles(userNew.getRoles());
                    mockMvc.perform(MockMvcRequestBuilders.patch("/user/dong").param("access_token", accessToken).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(userOld)))
                            .andDo(MockMvcResultHandlers.print())
                            .andExpect(status().isOk());
                });

        User userUpdate = new User();
        userUpdate.setBrandName("meidijituan");


        mockMvc.perform(MockMvcRequestBuilders.patch("/user/dong").param("access_token", accessToken).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(userUpdate)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    User updatedUser = mapper.readValue(json, User.class);
                    Assert.assertEquals("meidijituan", updatedUser.getBrandName());

                });

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/111").param("access_token", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void changePasswordTest() throws Exception {
        final String accessToken = obtainAccessToken();

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

        mockMvc.perform(post("/user").param("access_token", accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(root1)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/password/haha").param("access_token", accessToken)
                .param("username", "").param("oldPassword", "123").param("newPassword", "123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Assert.assertTrue(passwordEncoder.matches("123456", userService.getByUserName("haha").getPassword()));
        Assert.assertFalse(passwordEncoder.matches("123", userService.getByUserName("haha").getPassword()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/333").param("access_token", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }
}
