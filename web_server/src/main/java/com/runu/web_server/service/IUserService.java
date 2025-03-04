package com.runu.web_server.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.runu.web_server.entity.User;
import com.runu.web_server.tool.r.R;

public interface IUserService extends IService<User> {

    User getValidUser(String account);

    R signUp(User user);

    Page<User> pageQuery(
            Integer pageSize,
            Integer pageNum,
            String userName,
            String phone
    );

    R saveUser(User user);

    R setUser(User user);
}
