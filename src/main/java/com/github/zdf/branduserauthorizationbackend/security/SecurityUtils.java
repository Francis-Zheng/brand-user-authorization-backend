package com.github.zdf.branduserauthorizationbackend.security;

import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SecurityUtils {
    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    /**
     * 获取当前用户的角色集
     *
     * @return 当前用户角色集
     */
    public static Set<Role> getCurrentUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            logger.debug("当前已登录用户的角色集合为" + auth.getAuthorities());
            // 强制转换不应该出错的
            return new HashSet<>((Collection<? extends Role>) auth.getAuthorities());
        } else {
            logger.debug("当前无用户登录，返回空角色集合");
            return Collections.emptySet();
        }
    }

    @Nullable
    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated()) {
            return null;
        }

        return ((User) auth.getPrincipal());
    }

    /**
     * 返回当前用户是否是超级用户
     *
     * @return true代表是，false代表不是
     */
    public static boolean isSuperAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated()) {
            return false;
        }

        Collection<Role> authorities = (Collection<Role>) auth.getAuthorities();
        return authorities.parallelStream().anyMatch(role -> Constants.ROLE_SUPER_ADMIN.equals(role.getRoleName()));
    }
}
