package com.runu.web_server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import com.runu.web_server.config.security.SecurityUserDetail;
import com.runu.web_server.entity.Role;
import com.runu.web_server.entity.User;
import com.runu.web_server.service.IPowerService;
import com.runu.web_server.service.IUserService;
import com.runu.web_server.tool.JwtUtil;
import com.runu.web_server.tool.r.R;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.runu.web_server.entity.table.UserTableDef.USER;

@RestController
@RequestMapping
public class LoginController {

    @Resource
    private IUserService userService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JedisPool jedisPool;

    @Resource
    private IPowerService iPowerService;

    @PostMapping("login")
    public R login(
            @RequestBody User loginUser
    ) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUserAccount(),
                        loginUser.getUserPassword()
                );
        Authentication authentication = authenticationManager
                .authenticate(authenticationToken);

        if (Objects.isNull(authentication)) {
            return R.error("用户名或密码错误");
        }

        SecurityUserDetail userDetail = (SecurityUserDetail) authentication.getPrincipal();

        QueryWrapper qw = new QueryWrapper()
                .where(USER.USER_ACCOUNT.eq(userDetail.getUsername()));
        User userData = userService.getMapper().selectOneWithRelationsByQuery(qw);
        userData.setPowerCodes(
                iPowerService.getPowersByUserId(
                        userData.getUserAccount()
                )
        );
        userData.setUserPassword("");

        String jwt = JwtUtil.createToken(loginUser.getUserAccount());

        jedisPool.getResource().set(
                "pure:jwt:" + userData.getUserAccount(),
                JSON.toJSONString(userDetail)
        );

        jedisPool.getResource().expire(
                "pure:jwt:" + userData.getUserAccount(),
                24 * 60 * 60
        );

        Set<String> powerAndRoleCode = powerRolesMerge(
                userData.getPowerCodes(),
                userData.getRoles()
        );

        JSONObject rData = new JSONObject();
        rData.put("accessToken", jwt);
        rData.put("username", userData.getUserAccount());
        rData.put("nickname", userData.getUserName());
        rData.put(
                "roles",
                powerAndRoleCode
        );
        rData.put(
                "permissions",
                powerAndRoleCode
        );
        rData.put("expires",
                System.currentTimeMillis() +
                        (
                                jedisPool.getResource().ttl(
                                        "pure:jwt:" + userData.getUserAccount()
                                ) * 1000
                        )
        );
        return R.ok(rData);
    }


    private Set<String> powerRolesMerge(List<String> powers, List<Role> roles) {
        Set<String> powerRoles = new HashSet<>(powers);
        roles.forEach(role -> powerRoles.add(role.getRoleCode()));
        return powerRoles;
    }
}
