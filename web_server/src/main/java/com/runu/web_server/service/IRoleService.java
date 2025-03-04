package com.runu.web_server.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.runu.web_server.entity.Role;

public interface IRoleService extends IService<Role> {

    Page<Role> pageQuery(Integer pageSize,Integer pageNum,String roleName);
}
