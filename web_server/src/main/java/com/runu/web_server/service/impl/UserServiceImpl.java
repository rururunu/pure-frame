package com.runu.web_server.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.runu.web_server.entity.User;
import com.runu.web_server.mapper.UserMapper;
import com.runu.web_server.service.IPowerService;
import com.runu.web_server.service.IUserService;
import com.runu.web_server.tool.r.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static com.runu.web_server.entity.table.UserTableDef.USER;

@Slf4j
@Component
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements IUserService {

    @Resource
    private IPowerService powerService;

    @Override
    public User getValidUser(String account) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(
                        USER.USER_ACCOUNT.eq(account)
                );
        User user = mapper.selectOneWithRelationsByQuery(queryWrapper);
        user.setPowerCodes(powerService.getPowersByUserId(account));
        return user;
    }

    @Override
    public R signUp(User user) {
        User notUser = getValidUser(user.getUserAccount());
        if (notUser != null) {
            return R.error("账户已存在");
        }
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setUserPassword(
                    encoder.encode(user.getUserPassword())
            );
            return save(user) ?
                    R.ok("创建账户成功") :
                    R.error("创建账户失败");
        } catch (Exception e) {
            log.error("创建账户时出现异常", e);
            return R.error("创建账户时出现异常");
        }
    }

    @Override
    public Page<User> pageQuery(
            Integer pageSize,
            Integer pageNum,
            String userName,
            String phone
    ) {
        QueryWrapper queryWrapper = new QueryWrapper()
                .where(
                        USER.USER_NAME.like(userName)
                                .or(
                                        USER.USER_PHONE.like(phone)
                                )
                )
                .orderBy(USER.USER_NEW_DATE.asc());
        return mapper.paginateWithRelations(pageNum, pageSize, queryWrapper);
    }

    @Override
    public R saveUser(User user) {
        long count = this.queryChain()
                .where(USER.USER_ACCOUNT.eq(user.getUserAccount()))
                .count();
        if (count > 0) {
            return R.error("用户已存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setUserPassword(encoder.encode(user.getUserPassword()));
        return save(user) ? R.ok("添加用户成功") : R.error("添加用户失败");
    }

    @Override
    public R setUser(User user) {
        User userOld = getById(user.getUserId());
        if (
                !user.getUserPassword()
                        .equals(userOld.getUserPassword())
        ) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(user.getUserPassword());
            user.setUserPassword(password);
        }
        if (
                !user.getUserAccount()
                        .equals(userOld.getUserAccount())
        ) {
            long count = queryChain()
                    .where(USER.USER_ACCOUNT.eq(user.getUserAccount()))
                    .count();
            if (count > 0) {
                return R.error("账号已存在");
            }
        }
        user.setUserSetDate(new Date());
        return updateById(user) ?
                R.ok("修改成功") :
                R.error("修改失败");
    }
}
