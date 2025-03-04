package com.runu.web_server.pojo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 对应 elementTree 结构
 */
@Data
@Builder
public class ElTree implements Serializable {

    private String id;

    private String supId;

    private String label;

    private String value;

    private List<ElTree> children;
}
