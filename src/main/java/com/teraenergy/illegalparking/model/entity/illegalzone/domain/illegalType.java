package com.teraenergy.illegalparking.model.entity.illegalzone.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
@Entity
public class illegalType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer typeSeq;    // 키

    @Column
    String name;        // 불법 주정차 타입 이름 ( 예> 불법주정차/5분주차/탄력주차 )

    @Column
    Boolean isDel;      // 삭제 여부
}
