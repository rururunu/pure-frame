package com.runu.web_server.controller;

import com.runu.web_server.entity.Power;
import com.runu.web_server.service.IPowerService;
import com.runu.web_server.tool.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("power")
public class PowerController {

    @Resource
    private IPowerService powerService;

    @GetMapping
    @PreAuthorize("hasAuthority('power')")
    public R get() {
        return R.ok("获取权限成功", powerService.list());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('power')")
    public R getById(@PathVariable("id") String id) {
        return R.ok("获取权限成功", powerService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('power:set')")
    public R save(@RequestBody Power power) {
        return powerService.saveOrUpdate(power) ?
                R.ok("操作权限成功") :
                R.error("操作权限失败");
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('power:del')")
    public R del(@RequestBody Power power) {
        return powerService.delAndSupId(power);
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('power')")
    public R getTree() {
        return R.ok("获取成功", powerService.getTree());
    }

    @GetMapping("/elTree")
    @PreAuthorize("hasAuthority('power')")
    public R getElTree() {
        return R.ok("获取成功", powerService.getElTree());
    }
}
