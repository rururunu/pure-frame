package com.runu.web_server.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table("role")
public class Role {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    private String roleId;

    private String roleName;

    private String roleCode;

    private String note;

    @Column(isLogicDelete = true)
    private int isDelete;

    @RelationOneToMany(
            joinTable = "role_power",
            selfField = "roleId", joinSelfColumn = "role_id",
            targetField = "powerId", joinTargetColumn = "power_id"
    )
    private List<Power> powers;
}
