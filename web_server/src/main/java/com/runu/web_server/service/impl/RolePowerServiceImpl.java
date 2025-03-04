package com.runu.web_server.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.runu.web_server.entity.RolePower;
import com.runu.web_server.mapper.RolePowerMapper;
import com.runu.web_server.service.IRolePowerService;
import com.runu.web_server.tool.r.R;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.runu.web_server.entity.table.RolePowerTableDef.ROLE_POWER;

@Component
public class RolePowerServiceImpl
        extends ServiceImpl<RolePowerMapper, RolePower>
        implements IRolePowerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveRolePower(String roleId, String powersStr) {
        List<RolePower> rolePowers = new ArrayList<>();

        QueryWrapper qw = new QueryWrapper()
                .where(ROLE_POWER.ROLE_ID.eq(roleId));

        if (count(qw) > 0) {
            if (!remove(qw)) {
                throw new RuntimeException("清空过程失败");
            }
        }

        if (powersStr.isEmpty()) {
            return R.ok("设定成功");
        }

        String[] powers = powersStr.split(",");

        for (String power : powers) {
            rolePowers.add(
                    RolePower.builder()
                            .roleId(roleId)
                            .powerId(power)
                            .build()
            );
        }

        if (saveBatch(rolePowers)) {
            return R.ok("设定成功");
        } else {
            throw new RuntimeException("赋予权限失败");
        }
    }
}
