package com.runu.web_server.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table("role_power")
public class RolePower {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String roleId;

    private String powerId;
}
