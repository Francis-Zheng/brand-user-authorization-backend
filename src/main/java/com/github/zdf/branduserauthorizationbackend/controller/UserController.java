package com.github.zdf.branduserauthorizationbackend.controller;

import com.github.zdf.branduserauthorizationbackend.domain.User;
import com.github.zdf.branduserauthorizationbackend.service.UserService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口")
public class UserController extends BaseController<User, String> {
    private UserService userService;

    protected UserController(UserService service) {
        super(service, User.class);
        this.userService = service;
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")    等价
//    @PreAuthorize("hasRole('ADMIN')")         等价
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/get/{userName}", method = RequestMethod.GET)
    @ApiOperation(value = "根据用户名获取", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "没有找到对应的用户")
    })
    public ResponseEntity<User> getByUserName(@PathVariable("userName") @ApiParam("用户名") String userName) {
        if (userService.getByUserName(userName) != null)
            return new ResponseEntity<>(userService.getByUserName(userName), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "插入新的实体", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 201, message = "创建成功")
    })
    public ResponseEntity<User> saveUnique(@RequestBody @ApiParam(required = true, value = "新的实体") User entity) {
        return new ResponseEntity<>(userService.saveUnique(entity), HttpStatus.CREATED);
    }

    /**
     * 对于Map，则替换相同键的部分。对于数组或List
     * 则替换下标相同的部分，且将超长的部分加入到数组中。对于Set，则替换HashCode相同的部分。对于基本类型及其包装类，则
     * 替换值。其他的类型，则递归进入替换
     *
     * @param username  需要更新目标实体的username
     * @param updateVal 值
     * @return 更新后的值
     */
    @ApiOperation(value = "部分更新，只更新提交的实体对象中不是Null的部分")
    @RequestMapping(value = "/{username}", method = RequestMethod.PATCH)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功"),
            @ApiResponse(code = 400, message = "参数有误。请查看是否错误的修改了ID"),
            @ApiResponse(code = 404, message = "未找到实体。可能ID不正确"),
    })
    public ResponseEntity<User> partialUpdate(@PathVariable("username") String username, @RequestBody User updateVal) {
        return new ResponseEntity<>(userService.partialUpdate(username, updateVal), HttpStatus.OK);
    }

    @RequestMapping(value = "/password/{username}", method = RequestMethod.PATCH)
    @ApiOperation(value = "更新用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", dataType = "String", required = true, paramType = "path"),
            @ApiImplicitParam(name = "oldPassword", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "newPassword", dataType = "String", required = true, paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "密码更改成功"),
            @ApiResponse(code = 400, message = "参数有误"),
            @ApiResponse(code = 404, message = "未找到实体，可能username不正确"),
    })
    public ResponseEntity<?> changeUserPassword(@PathVariable("username") String username, String oldPassword, String newPassword) {
        return new ResponseEntity<>(userService.changeUserPassword(username, oldPassword, newPassword), HttpStatus.OK);
    }


}
