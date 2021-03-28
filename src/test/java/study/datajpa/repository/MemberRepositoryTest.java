package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @DisplayName("Method 이름으로 쿼리 생성 > save / findById 테스트")
    @Test
    void testMember() {
        Member member = new Member("testMember");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @DisplayName("@NamedQuery 테스트")
    @Test
    void findByUsername() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @DisplayName("@Query 테스트")
    @Test
    void findUser() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @DisplayName("@Query 테스트")
    @Test
    void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @DisplayName("@Query 테스트 > DTO 조회")
    @Test
    void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> usernameList = memberRepository.findMemberDto();

        for (MemberDto memberDto : usernameList) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @DisplayName("@Query 테스트 > IN 절 조회")
    @Test
    void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> memberList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member member : memberList) {
            System.out.println("member = " + member);
        }
    }

    @DisplayName("@Query 테스트 > 반환 타입")
    @Test
    void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // * List 는 빈값인 경우 '0' 으로 반환 (null 반환 아님 !) > if (result != null) 코드 작성 X
        List<Member> member = memberRepository.findListByUsername("ABCD");
        System.out.println("member = " + member);
        // * 빈값인 경우, 'null' 반환
        Member findMember = memberRepository.findMemberByUsername("ABCD");
        System.out.println("findMember = " + findMember);
        // * 빈값인 경우, Optional.empty
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("ABCD");
        System.out.println("optionalMember = " + optionalMember);
    }

    @DisplayName("Spring Data JPA > Paging")
    @Test
    void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member2", 10));

        int age = 10;
        // Spring Data 에서는 page - 0 부터 시작
        // PageRequest - Pageable 구현체
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);     // 반환 타입이 Page 라면, Count 쿼리 자동 실행
        // Slice<Member> page = memberRepository.findByAge(age, pageRequest); // Total Count 쿼리를 실행하지 않음

        // * Page -> DTO 변환 방법
        // Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null);

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);          // content size
        assertThat(page.getTotalElements()).isEqualTo(5); // total count
        assertThat(page.getNumber()).isEqualTo(0);        // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);    // 총 페이지 개수
        assertThat(page.isFirst()).isTrue();              // 현재 페이지가 첫번째 페이지인지 ?
        assertThat(page.hasNext()).isTrue();              // 다음 페이지가 존재하는지 ?
    }

    @DisplayName("Bulk Update > 나이가 20 이상인 회원의 나이를 + 1 Bulk Update")
    @Test
    void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20)); // 21
        memberRepository.save(new Member("member4", 21)); // 22
        memberRepository.save(new Member("member5", 40)); // 41

        // when
        int result = memberRepository.bulkAgePlus(20);

        // * 주의할 점 : 현재 시점에 member5 를 조회할 경우, DB 에는 age - 41 로 반영되어 있겠지만,
        // 영속성 컨텍스트 안에는 member5가 age - 40 으로 남아있음
        // - 따라서, Bulk 연산 이후에는 영속성 컨텍스트를 다 날려줘야함 (중요)
        // - 아래와 같이 em.clear() 를 호출하거나 @Modifying 어노테이션의 clearAutomatically 옵션을 true 로 설정
        // em.clear();

        List<Member> member = memberRepository.findByUsername("member5");
        System.out.println("member = " + member.get(0));

        // then
        assertThat(result).isEqualTo(3);
    }
}