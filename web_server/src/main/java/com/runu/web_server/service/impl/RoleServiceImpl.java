package com.runu.web_server.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.runu.web_server.entity.Role;
import com.runu.web_server.mapper.RoleMapper;
import com.runu.web_server.service.IRoleService;
import org.springframework.stereotype.Component;

import static com.runu.web_server.entity.table.RoleTableDef.ROLE;

@Component
public class RoleServiceImpl
        extends ServiceImpl<RoleMapper, Role>
        implements IRoleService {

    @Override
    public Page<Role> pageQuery(Integer pageSize, Integer pageNum, String roleName) {
        QueryWrapper queryWrapper = new QueryWrapper()
                .where(ROLE.ROLE_NAME.like(roleName));
        return mapper.paginateWithRelations(pageNum, pageSize, queryWrapper);
    }
}
