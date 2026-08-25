package com.study.studyproject.postlike.repository;

import com.study.studyproject.board.domain.Board;
import com.study.studyproject.board.domain.Category;
import com.study.studyproject.board.repository.BoardRepository;
import com.study.studyproject.member.domain.Member;
import com.study.studyproject.member.repository.MemberRepository;
import com.study.studyproject.postlike.domain.PostLike;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static com.study.studyproject.auth.domain.Role.ROLE_USER;
import static com.study.studyproject.board.domain.Category.CS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// 의도적으로 클래스 레벨 @Transactional을 붙이지 않는다: 아래 테스트는
// 유니크 제약 위반으로 save()가 예외를 던지는 상황을 재현하는데, 같은
// 트랜잭션(=같은 Hibernate Session)에서 예외가 발생하면 그 Session은
// 이후 어떤 작업도 할 수 없는 상태가 되어 @AfterEach의 정리 쿼리까지
// 실패한다. 각 리포지토리 호출이 자신만의 트랜잭션으로 커밋/롤백되도록
// 두어야 정리(@AfterEach)가 새 세션에서 정상적으로 동작한다.
@SpringBootTest
class PostLikeRepositoryTest {

    @Autowired
    PostLikeRepository postLikeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("같은 회원-게시글 조합의 관심글을 두 번 저장하면 DB 제약 위반으로 실패한다.")
    void duplicatePostLikeViolatesUniqueConstraint() throws Exception {
        //given
        Member member1 = createMember("jacom2@naver.com", "1234", "사용자명1", "닉네임1");
        Board boardCreate = createBoard(member1, "제목1", CS);
        memberRepository.save(member1);
        boardRepository.save(boardCreate);
        postLikeRepository.save(PostLike.create(member1, boardCreate));

        //when & then
        // 서비스 계층의 존재 여부 체크(findByBoardAndMember)를 거치지 않고
        // 곧바로 저장을 시도해, 동시 요청이 체크를 동시에 통과한 상황을 재현한다.
        assertThatThrownBy(() -> postLikeRepository.save(PostLike.create(member1, boardCreate)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Member createMember(String email, String password, String username, String nickname) {
        return Member.builder()
                .nickname(nickname)
                .username(username)
                .email(email)
                .password(password)
                .role(ROLE_USER).build();
    }

    private Board createBoard(Member member, String title, Category category) {
        return Board.builder()
                .member(member)
                .title(title)
                .content("내용")
                .category(category)
                .build();
    }
}
