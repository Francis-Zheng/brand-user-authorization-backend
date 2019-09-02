package com.github.zdf.branduserauthorizationbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zdf.branduserauthorizationbackend.exception.EntityNotExistException;
import com.github.zdf.branduserauthorizationbackend.exception.MethodNotSupportException;
import com.github.zdf.branduserauthorizationbackend.service.BaseService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class BaseController<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger("BaseController");

    private BaseService<T, ID> service;
    private Class<T> tClass;
    @Autowired
    private ObjectMapper mapper;

    protected BaseController(BaseService<T, ID> service, Class<T> tClass) {
        this.service = service;
        this.tClass = tClass;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取对应实体", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "实体ID", paramType = "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "未找到对应的实体")
    })
    public ResponseEntity<T> getById(@PathVariable("id") ID id) {
        Optional<T> entity = service.getById(id);
        return entity.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "获取所有实体", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<T>> getAll() {
        return new ResponseEntity<>(new ArrayList<>(((Collection<T>) service.getAll())), HttpStatus.OK);
    }

    @ApiOperation(value = "删除实体", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<T> delete(@RequestBody @ApiParam(value = "需要删除的实体，比较重要的是要有ID") T entity) {
        service.delete(entity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "根据ID删除实体", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(type = "path", value = "要删除的实体ID", name = "id", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<T> deleteById(@PathVariable("id") ID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "将传入的对象转变为查询条件，查询符合的所有实体对象", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 400, message = "请求不正确，可能由于example的格式错误，需要是经过URL编码的JSON格式")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, params = {"action=query"})
    @ApiParam(name = "action", allowableValues = "query", required = true, value = "查询API的操作参数")
    public ResponseEntity<List<T>> getAllByExample(@RequestParam(name = "example") @ApiParam(value = "查询的条件，是实体对象的JSON表示", required = true) String example) {
        T t;
        try {
            t = mapper.readValue(example, tClass);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ArrayList<>(((Collection<T>) service.getAllByExample(t))), HttpStatus.OK);
    }

    @ApiOperation(value = "注销", httpMethod = "GET")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodNotSupportException.class})
    public ResponseEntity<Exception> illegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotExistException.class})
    public ResponseEntity<Exception> entityNotFound(EntityNotExistException e) {
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Exception> exception(Exception e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
