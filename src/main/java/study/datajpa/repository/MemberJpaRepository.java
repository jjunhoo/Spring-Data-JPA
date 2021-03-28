package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext // Spring Boot 에서 주입
    private EntityManager em;

    public Member save (Member member) {
        em.persist(member);
        return member;
    }

    public void delete (Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsername(String username) {
        // Member Entity 에 @NamedQuery 로 정의해둔 쿼리 사용 가능
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "memberA")
                .getResultList();
    }
}
