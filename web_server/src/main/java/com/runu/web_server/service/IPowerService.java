package com.runu.web_server.service;

import com.mybatisflex.core.service.IService;
import com.runu.web_server.entity.Power;
import com.runu.web_server.pojo.ElTree;
import com.runu.web_server.tool.r.R;

import java.util.List;

public interface IPowerService extends IService<Power> {

    List<String> getPowersByUserId(String account);

    R delAndSupId(Power power);

    List<Power> getTree();

    List<ElTree> getElTree();
}
