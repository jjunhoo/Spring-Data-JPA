package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberJpaRepository {

    @PersistenceContext // Spring Boot 에서 주입
    private EntityManager em;

    public Member save (Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
