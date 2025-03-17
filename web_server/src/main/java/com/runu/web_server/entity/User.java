package com.runu.web_server.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table("user")
public class User {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private String userId;

    private String userName;

    private String userAccount;

    private String userPassword;

    private Date userNewDate;

    @Column(onUpdateValue = "now()")
    private Date userSetDate;

    private String userNewUserName;

    @Column(isLogicDelete = true)
    private Boolean userIsDelete;

    private String userHead;

    private String userPhone;

    private String userEmail;

    @RelationOneToMany(
            joinTable = "user_role",
            selfField = "userId", joinSelfColumn = "user_id",
            targetField = "roleId", joinTargetColumn = "role_id"
    )
    private List<Role> roles;

    @Column(ignore = true)
    private List<String> powerCodes;
}
