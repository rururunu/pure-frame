package com.runu.web_server.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.runu.web_server.entity.UserRole;
import com.runu.web_server.mapper.UserRoleMapper;
import com.runu.web_server.service.IUserRoleService;
import com.runu.web_server.tool.r.R;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.runu.web_server.entity.table.UserRoleTableDef.USER_ROLE;

@Component
public class UserRoleServiceImpl
        extends ServiceImpl<UserRoleMapper, UserRole>
        implements IUserRoleService {

    @Override
    @Transactional
    public R saveUserRole(String userId, String roleIdStr) {
        List<UserRole> userRoles = new ArrayList<>();
        QueryWrapper qw = new QueryWrapper()
                .where(
                        USER_ROLE.USER_ID.eq(userId)
                );
        if (count(qw) > 0) {
            if (!remove(qw)) {
                return R.error("删除角色过程失败");
            }
        }
        if (roleIdStr.isEmpty()) {
            return R.ok("操作成功");
        }
        String[] roleIds = roleIdStr.split(",");
        for (String roleId : roleIds) {
            userRoles.add(
                    UserRole.builder()
                            .roleId(roleId)
                            .userId(userId)
                            .build()
            );
        }
        return saveBatch(userRoles) ? R.ok("操作成功") : R.error("操作失败");
    }
}
