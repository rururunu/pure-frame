package com.runu.web_server.controller;

import com.mybatisflex.core.query.QueryWrapper;
import com.runu.web_server.entity.Role;
import com.runu.web_server.service.IRoleService;
import com.runu.web_server.tool.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.runu.web_server.entity.table.RoleTableDef.ROLE;

@RestController
@RequestMapping("role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('role:set')")
    public R save(@RequestBody Role role) {
        return roleService.saveOrUpdate(role) ?
                R.ok("操作成功") :
                R.error("操作失败");
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('role:del')")
    public R del(@PathVariable String id) {
        return roleService.removeById(id) ?
                R.ok("删除角色成功") :
                R.error("删除角色失败");
    }

    @GetMapping
    public R get() {
        return R.ok(
                "获取所有角色成功",
                roleService.list()
        );
    }

    @GetMapping("/{id}")
    public R getRoleById(@PathVariable String id) {
        return R.ok(
                "获取角色信息成功",
                roleService.getMapper().selectOneWithRelationsByQuery(
                        new QueryWrapper().where(ROLE.ROLE_ID.eq(id))
                )
        );
    }

    @GetMapping("page")
    public R getPageByQuery(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "roleName", defaultValue = "") String roleName
    ) {
        return R.ok(
                "获取角色分页成功",
                roleService.pageQuery(
                        pageSize,
                        pageNum,
                        roleName
                )
        );
    }
}
