package com.github.zdf.branduserauthorizationbackend.security;
public class Constants {
    /**
     * 后台管理员角色
     */
    public static final String ROLE_SUPER_ADMIN = "ROLE_ADMIN"; //在spring security 5中，必须以“ROLE_”前缀开头。

    /**
     * 政府角色
     */
    public static final String ROLE_GOVERNMENT_USER = "ROLE_GOVERNMENT";

    /**
     * 企业角色
     */
    public static final String ROLE_COMPANY_USER = "ROLE_COMPANY";

    /**
     * 专家角色
     */
    public static final String ROLE_EXPERT = "ROLE_EXPERT";
}
