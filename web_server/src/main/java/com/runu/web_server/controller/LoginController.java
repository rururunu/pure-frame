package com.runu.web_server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runu.web_server.config.security.SecurityUserDetail;
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
import java.util.Objects;

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

        User userData = userService.queryChain()
                .where(USER.USER_ACCOUNT.eq(userDetail.getUsername()))
                .one();
        userData.setPowerCodes(
                iPowerService.getPowersByUserId(
                        userData.getUserAccount()
                )
        );
        userData.setUserPassword("");

        String jwt = JwtUtil.createToken(loginUser.getUserAccount());

        jedisPool.getResource().set(
                "pure:jwt:" + userData.getUserAccount(),
                JSON.toJSONString(userData)
        );

        jedisPool.getResource().expire(
                "pure:jwt:" + userData.getUserAccount(),
                24 * 60 * 60
        );


        JSONObject rData = new JSONObject();
        rData.put("accessToken", jwt);
        rData.put("username", userData.getUserAccount());
        rData.put("nickname", userData.getUserName());
        rData.put("roles", userData.getPowerCodes());
        rData.put("powers", userData.getPowerCodes());
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

}
