package com.teraenergy.illegalparking.model.entity.receipt.repository;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
