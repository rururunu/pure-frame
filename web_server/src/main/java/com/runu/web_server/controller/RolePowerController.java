package com.runu.web_server.controller;

import com.runu.web_server.service.IRolePowerService;
import com.runu.web_server.tool.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("role_power")
public class RolePowerController {

    @Resource
    private IRolePowerService rolePowerService;

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('role')")
    public R save(String roleId, String powerStr) {
        return rolePowerService.saveRolePower(roleId, powerStr);
    }
}
