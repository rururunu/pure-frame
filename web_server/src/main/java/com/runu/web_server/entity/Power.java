package com.runu.web_server.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table("power")
public class Power {

    @Id(keyType = KeyType.Auto)
    private Integer powerId;

    private Integer powerSupId;

    private String powerCode;

    private String powerName;

    @Column(isLogicDelete = true)
    private Boolean isDelete;

    @Column(ignore = true)
    private List<Power> children;
}
