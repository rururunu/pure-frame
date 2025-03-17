package com.runu.web_server.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.runu.web_server.entity.Power;
import com.runu.web_server.entity.User;
import com.runu.web_server.mapper.PowerMapper;
import com.runu.web_server.mapper.UserMapper;
import com.runu.web_server.pojo.ElTree;
import com.runu.web_server.service.IPowerService;
import com.runu.web_server.tool.r.R;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.runu.web_server.entity.table.PowerTableDef.POWER;
import static com.runu.web_server.entity.table.UserTableDef.USER;

@Component
public class PowerServiceImpl
        extends ServiceImpl<PowerMapper, Power>
        implements IPowerService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<String> getPowersByUserId(String account) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(USER.USER_ACCOUNT.eq(account));
        User user = userMapper
                .selectOneWithRelationsByQuery(queryWrapper);
        List<String> powers = new ArrayList<>();
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                if (role.getPowers() != null) {
                    role.getPowers().forEach(power -> {
                        powers.add(power.getPowerCode());
                    });
                }
            });
        }
        return powers;
    }

    @Override
    public R delAndSupId(int id) {
        Power power = getById(id);
        QueryWrapper qw = new QueryWrapper()
                .where(
                        POWER.POWER_ID.eq(power.getPowerId())
                                .or(
                                        POWER.POWER_SUP_ID.eq(power.getPowerId())
                                )
                );
        return remove(qw) ?
                R.ok("删除权限及子权限成功") :
                R.error("删除权限及子权限失败");
    }

    @Override
    public List<Power> getTree() {
        return toTreePower(list());
    }

    @Override
    public List<ElTree> getElTree() {
        return getElTree(list());
    }

    private List<Power> toTreePower(List<Power> powers) {
        Map<Integer, List<Power>> powerMap = powers
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Power::getPowerSupId
                        )
                );
        powers.forEach(power -> power.setChildren(
                        powerMap.get(power.getPowerId())
                )
        );
        return powers.stream()
                .filter(item ->
                        item.getPowerSupId() == 0
                )
                .collect(Collectors.toList());
    }

    private List<ElTree> getElTree(List<Power> powers) {
        List<ElTree> elTrees = powers.stream()
                .map(power ->
                        ElTree.builder()
                                .id(String.valueOf(power.getPowerId()))
                                .supId(String.valueOf(power.getPowerSupId()))
                                .label(power.getPowerName())
                                .value(String.valueOf(power.getPowerId()))
                                .build()
                ).collect(Collectors.toList());
        elTrees.forEach(elTree -> elTree.setChildren(
                elTrees.stream()
                        .collect(
                                Collectors.groupingBy(
                                        ElTree::getSupId
                                )
                        ).get(elTree.getId())
        ));
        return elTrees.stream()
                .filter(item ->
                        item.getSupId().equals("0") ||
                                item.getSupId().isEmpty()
                )
                .collect(Collectors.toList());
    }
}
