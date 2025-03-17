package com.runu.web_server.controller;

import com.mybatisflex.core.query.QueryWrapper;
import com.runu.web_server.entity.User;
import com.runu.web_server.service.IUserService;
import com.runu.web_server.tool.r.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.runu.web_server.entity.table.UserTableDef.USER;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    @PutMapping("signUp")
    public R signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @GetMapping("all")
    @PreAuthorize("hasAuthority('user')")
    public R allUser() {
        return R.ok("获取成功", userService.list());
    }

    @GetMapping("pageByQuery")
    @PreAuthorize("hasAuthority('user')")
    public R pageByQuery(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "userName", defaultValue = "") String userName,
            @RequestParam(value = "phone", defaultValue = "") String phone
    ) {
        return R.ok(
                "获取成功",
                userService.pageQuery(
                        pageSize,
                        pageNum,
                        userName,
                        phone
                )
        );
    }

    @PutMapping
    @PreAuthorize("hasAuthority('user:add')")
    public R putUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:set')")
    public R postUser(@RequestBody User user) {
        return userService.setUser(user);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('user:del')")
    public R delUser(@PathVariable String id) {
        return userService.removeById(id) ?
                R.ok("删除成功") :
                R.error("删除失败");
    }

    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAuthority('user')")
    public R getById(@PathVariable String id) {
        return R.ok(
                "获取用户成功",
                userService
                        .getMapper()
                        .selectOneWithRelationsByQuery(
                                new QueryWrapper()
                                        .where(USER.USER_ID.eq(id))
                        )
        );
    }
}
