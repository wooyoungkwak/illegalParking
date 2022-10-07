package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Comment;
import com.teraenergy.illegalparking.model.entity.receipt.domain.QComment;
import com.teraenergy.illegalparking.model.entity.receipt.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class CommentServiceImple implements CommentService{

    private final CommentRepository commentRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> gets(Integer receiptSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QComment.comment);
        query.where(QComment.comment.receiptSeq.eq(receiptSeq));
        return query.fetch();
    }

    @Override
    public Comment set(Comment comment) {
        return commentRepository.save(comment);
    }

}
