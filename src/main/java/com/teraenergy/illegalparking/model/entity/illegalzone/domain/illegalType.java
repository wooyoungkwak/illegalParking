package com.teraenergy.illegalparking.model.entity.illegalzone.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : springboot-illegalPaking
 * Description :
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class illegalType {
    Integer typeSeq;    // 키
    String name;        // 불법 주정차 타입 이름 ( 예> 불법주정차/5분주차/탄력주차 )
    Boolean isDel;      // 삭제 여부
}
