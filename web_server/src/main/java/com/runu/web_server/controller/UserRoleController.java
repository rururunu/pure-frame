package com.runu.web_server.controller;

import com.runu.web_server.service.IUserRoleService;
import com.runu.web_server.tool.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("user_role")
public class UserRoleController {

    @Resource
    private IUserRoleService userRoleService;

    @PutMapping
    @PreAuthorize("hasAuthority('user')")
    public R saveUserRole(
            @RequestParam String userId,
            @RequestParam String roleIdStr
    ) {
        return userRoleService.saveUserRole(userId, roleIdStr);
    }
}
