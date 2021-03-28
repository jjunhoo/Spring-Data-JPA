package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // Method 이름으로 쿼리 생성
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // @NamedQuery 사용법 > Member Entity 에 정의해둔 @NamedQuery 호출 가능
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // @Query 사용법
    @Query("select m from Member m where m.username = :username and m.age = :age") // Parameter Binding -> :username 에 @Param("username") 으로 받은 파라미터가 바인딩
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // @Query 사용법
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // @Query 사용법 (DTO 로 반환하는 방법)
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // @Query 사용법 (IN 절 사용법)
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // return type (repository-query-return-types 참고)
    List<Member> findListByUsername(String username); // 반환 타입 : 컬렉션
    Member findMemberByUsername(String username); // 반환 타입 : 단건
    Optional<Member> findOptionalByUsername(String username); // 반환 타입 : 단건 optional
}
