package com.teraenergy.illegalparking.model.dto.user.domain;

import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Getter
@Setter
public class UserDto {
    Integer userSeq;
    private String username;
    private String name;
    private Role role;
    private Long userCode;
}
