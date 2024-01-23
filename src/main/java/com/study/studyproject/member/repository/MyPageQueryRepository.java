package com.study.studyproject.member.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.studyproject.list.dto.ListResponseDto;
import com.study.studyproject.entity.Board;
import com.study.studyproject.entity.Category;
import com.study.studyproject.entity.Recruit;
import com.study.studyproject.list.dto.QListResponseDto;
import com.study.studyproject.member.dto.MemberListRequestDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.studyproject.entity.QBoard.board;
import static com.study.studyproject.entity.QMember.member;
import static com.study.studyproject.entity.QReply.reply;
import static org.springframework.util.StringUtils.isEmpty;

@Repository
public class MyPageQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MyPageQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<ListResponseDto> MyPageListPage(MemberListRequestDto condition, Pageable pageable) {

        List<ListResponseDto> content = getContent(condition, pageable);

        JPAQuery<Board> countQuery = getTotal(condition);

        return PageableExecutionUtils.getPage(content,pageable, countQuery::fetchCount);
    }

    public List<ListResponseDto> getContent(MemberListRequestDto condition, Pageable pageable) {

        return queryFactory
                .select(
                        new QListResponseDto(
                                board.nickname,
                                board.id.intValue(),
                                board.recruit.stringValue(),
                                board.category.stringValue(),
                                board.content,
                                board.title,
                                board.createdDate,
                                board.viewCount.intValue(),
                                reply.count().intValue()
                        )

                )
                .from(board)
                .leftJoin(reply)
                .on(board.id.eq(reply.board.id))
                .where(
                        getType(condition.getType()), //모집여부
                        getUser(condition.getMemberId()), //사용자 아이디 유무
                        getCategory(condition.getCategory())
                )
                .orderBy(
                        getOrder(condition.getOrder()) // 순서
                )
                .groupBy(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    //cs
    private BooleanExpression getCategory(Category category) {
        if(isEmpty(category)){
            return null;
        }

        return category.equals(Category.전체) ? null : board.category.eq(category);
    }

    public JPAQuery<Board> getTotal(MemberListRequestDto condition) {
        return queryFactory
                .select(
                        board
                )
                .from(board)
                .where(
                        getType(condition.getType()), //모집여부
                        getUser(condition.getMemberId()), //사용자 이메일
                        getCategory(condition.getCategory())
                );
    }


    private BooleanExpression getType(Recruit type) {


        return isEmpty(type) ? null : board.recruit.eq(type);
    }



    private BooleanExpression getUser(Long userId) {

        return isEmpty(userId) ? null : board.member.id.eq(userId);
    }

    private OrderSpecifier<?> getOrder(int num) {
        if (num == 0) {
            System.out.println("0");
            return board.createdDate.desc();
        } else {
            System.out.println("1");
             return board.viewCount.desc();
        }
    }


}
