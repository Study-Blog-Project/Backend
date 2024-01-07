package com.study.studyproject.board.dto;

import com.study.studyproject.entity.Board;
import com.study.studyproject.entity.Category;
import com.study.studyproject.entity.Member;
import com.study.studyproject.entity.Recruit;
import lombok.Data;
import org.hibernate.boot.archive.scan.spi.ClassDescriptor;


//Member member, String title, Long viewCount, String content, String nickname, Category category
@Data
public class BoardWriteRequestDto {

    //모집구분
    Recruit recruit;
    String content;
    Category category;
    String title;



    public Board toEntity(Member member) {
        return Board.builder()
                .member(member)
                .title(title)
                .content(content)
                .category(category)
                .build();
    }


}
