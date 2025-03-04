package com.runu.web_server.controller;

import com.runu.web_server.service.IUserRoleService;
import com.runu.web_server.tool.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("user_role")
public class UserRoleController {

    @Resource
    private IUserRoleService userRoleService;

    @PostMapping
    @PreAuthorize("hasAuthority('user')")
    public R saveUserRole(String userId, String roleIdStr) {
        return userRoleService.saveUserRole(userId, roleIdStr);
    }
}
