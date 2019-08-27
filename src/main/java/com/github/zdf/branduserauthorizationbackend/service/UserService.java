package com.github.zdf.branduserauthorizationbackend.service;

import com.github.zdf.branduserauthorizationbackend.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService extends BaseService<User, String> {
    /**
     * 根据用户名获取用户信息
     * @param userName 要查询的用户名
     * @return 查询到的用户
     */
    User getByUserName(String userName);

    /**
     * 获取某个企业品牌的所有用户
     * @param brandName 要查询的品牌名
     * @return 查询到的该品牌的所有用户的列表
     */
    List<User> getByBrandName(String brandName);

    /**
     * 根据用户名删除用户
     * @param userName 要删除的用户名
     */
    void deleteByUserName(String userName);

    /**
     * 删除某个品牌的所有用户
     * @param brandName 要删除的品牌
     */
    void deleteByBrandName(String brandName);
}
