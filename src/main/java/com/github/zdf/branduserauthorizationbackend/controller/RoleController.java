package com.github.zdf.branduserauthorizationbackend.controller;

import com.github.zdf.branduserauthorizationbackend.domain.Role;
import com.github.zdf.branduserauthorizationbackend.service.RoleService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理接口")
public class RoleController extends BaseController<Role, String> {
    private RoleService roleService;

    protected RoleController(RoleService service) {
        super(service, Role.class);
        this.roleService = service;
    }

    @RequestMapping(value = "/get/{roleName}", method = RequestMethod.GET)
    @ApiOperation(value = "根据角色名获取角色", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "没有找到对应的角色")
    })
    public ResponseEntity<Role> getByRoleName(@PathVariable("roleName") @ApiParam("角色名") String roleName) {
        return new ResponseEntity<>(roleService.getByRoleName(roleName), HttpStatus.OK);
    }


    @RequestMapping(value = "/delete/{roleName}", method = RequestMethod.DELETE)
    @ApiOperation(value = "根据角色名删除角色", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    public ResponseEntity<String> deleteByRoleName(@PathVariable("roleName") @ApiParam("角色名") String roleName) {
        roleService.deleteByRoleName(roleName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "插入新的实体", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 201, message = "创建成功")
    })
    public ResponseEntity<Role> saveUnique(@RequestBody @ApiParam(required = true, value = "新的实体") Role entity) {
        return new ResponseEntity<>(roleService.saveUnique(entity), HttpStatus.CREATED);
    }
}
