package com.github.zdf.branduserauthorizationbackend.service.impl;

import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.exception.EntityExistException;
import com.github.zdf.branduserauthorizationbackend.exception.EntityNotExistException;
import com.github.zdf.branduserauthorizationbackend.repository.UserRepository;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import com.github.zdf.branduserauthorizationbackend.utils.UtilFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {
    private UserRepository userRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    protected UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUnique(User user) throws EntityExistException {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new EntityExistException("已存在用户名为" + user.getUsername() + "的用户");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));//对明文密码进行加密
            return userRepository.save(user);
        }
    }

    @Override
    public User partialUpdate(String username, User updateVal) {
        updateVal.setPassword(null);//不允许使用此接口更新密码
        Optional<User> byUserName = Optional.ofNullable(getByUserName(username));
        User entity = byUserName.orElseThrow(() -> new EntityNotExistException("没有username为" + username + "的实体"));
        UtilFunctions.partialChange(entity, updateVal, null);
//        // 检查ID是否被更改了
//        if (!isIdOfEntity(username, entity)) {
//            throw new IllegalArgumentException("不允许修改实体ID");
//        }
        return userRepository.save(entity);
    }

    @Override
    public boolean changeUserPassword(String username, String oldPassword, String newPassword) {
        if (username.isEmpty()) {
            throw new BadCredentialsException("用户名不能为空");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("未找到该用户" + username);
        }

        if (user.getPassword() != null) {
            boolean match = passwordEncoder.matches(oldPassword == null ? "" : oldPassword, user.getPassword());
            if (!match) {
                throw new BadCredentialsException("用户名或密码错误");
            }
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }


    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUsername(userName);

    }

    @Override
    public List<User> getByBrandName(String brandName) {
        return userRepository.findByBrandName(brandName);
    }

    @Override
    public void deleteByUserName(String userName) {
        userRepository.deleteByUsername(userName);
    }

    @Override
    public void deleteByBrandName(String brandName) {
        userRepository.deleteByBrandName(brandName);
    }

    @Override
    public String getId(User entity) {
        return entity.getUserId();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (userRepository.findByUsername(s) == null) {
            throw new UsernameNotFoundException("没有用户名为" + s + "的用户");
        } else {
            return userRepository.findByUsername(s);
        }
    }


}
