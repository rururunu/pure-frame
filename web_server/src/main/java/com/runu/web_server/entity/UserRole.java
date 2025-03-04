package com.runu.web_server.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table("user_role")
public class UserRole {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String userId;

    private String roleId;
}
