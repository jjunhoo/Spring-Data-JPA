package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.Member;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional // JPA 테스트를 위해 Transactional 필요
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.find(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // 영속성 컨텍스트의 보장성 때문에 member 와 findMember 의 동일성을 보장
    }
}