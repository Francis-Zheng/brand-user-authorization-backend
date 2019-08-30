package com.github.zdf.branduserauthorizationbackend.service;

import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.exception.EntityExistException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends BaseService<User, String>, UserDetailsService {
    /**
     * 根据用户名获取用户信息
     *
     * @param userName 要查询的用户名
     * @return 查询到的用户
     */
    User getByUserName(String userName);

    /**
     * 获取某个企业品牌的所有用户
     *
     * @param brandName 要查询的品牌名
     * @return 查询到的该品牌的所有用户的列表
     */
    List<User> getByBrandName(String brandName);

    /**
     * 根据用户名删除用户
     *
     * @param userName 要删除的用户名
     */
    void deleteByUserName(String userName);

    /**
     * 删除某个品牌的所有用户
     *
     * @param brandName 要删除的品牌
     */
    void deleteByBrandName(String brandName);

    /**
     * 如果已经存在相同的用户名username，就更新原来的实体，而不是新创建一个相同username的实体
     *
     * @param user
     */
    User saveUnique(User user) throws EntityExistException;

    /**
     * 部分更新实体。仅仅使用updateVal中非null的部分进行更新。
     *
     * 注意：不允许使用此接口更新密码！！
     *
     * @param username        用户名
     * @param newVal 具有新的值的实体
     * @return 更新后的实体
     */
    User partialUpdate(String username, User newVal);

    boolean changeUserPassword(String username, String oldPassword, String newPassword);
}
