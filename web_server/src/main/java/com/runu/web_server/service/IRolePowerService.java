package com.runu.web_server.service;

import com.mybatisflex.core.service.IService;
import com.runu.web_server.entity.RolePower;
import com.runu.web_server.tool.r.R;

public interface IRolePowerService extends IService<RolePower> {

    R saveRolePower(String roleId, String powersStr);

}
