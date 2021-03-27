package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
