package com.runu.web_server.service;

import com.mybatisflex.core.service.IService;
import com.runu.web_server.entity.UserRole;
import com.runu.web_server.tool.r.R;

public interface IUserRoleService extends IService<UserRole> {

    R saveUserRole(String userId, String roleIdStr);
}
