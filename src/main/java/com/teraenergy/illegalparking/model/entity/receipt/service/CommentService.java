package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Comment;

import java.util.List;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface CommentService {

    List<Comment> gets(Integer receiptSeq);

    Comment set(Comment comment);
}
