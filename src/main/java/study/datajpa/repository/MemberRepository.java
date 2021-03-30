package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
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

    // Paging - count 쿼리 분리 (count 쿼리 최적화를 위한 분리 > count 쿼리 시 불필요 join 제거)
    @Query(value = "select m from Member m left join m.team t",
           countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    // Bulk Update
    // Spring Data JPA 에서 Bulk Update 시 @Modifying 어노테이션 추가 필요 / 없을 경우, getResultList 또는 getSingleResult 반환
    @Modifying(clearAutomatically = true) // bulk 연산이 끝난 후 영속성 컨텍스트를 자동 clear 하는 옵션
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // Fetch Join - Team 을 Lazy 로 셋팅해도, 한번에 조회 가능 (Team 의 데이터도 모두 채워줌)
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // [ @EntityGraph ]
    /*
     * 간단한 쿼리에 fetch join 을 사용해야 할 경우 사용
     * - 복잡한 쿼리가 필요한 경우, JPQL 에서 직접 fetch join 하는 방식으로 사용하는 편이 편리
     */
    // @EntityGraph 사용법 1) - Query Method + EntityGraph
    @Override
    @EntityGraph(attributePaths = {"team"}) // @EntityGraph - 메소드 이름을 통해 쿼리를 생성할 때, fetch join 을 같이 사용하고자 하는 상황에 사용
    List<Member> findAll();

    // @EntityGraph 사용법 2) - JPQL + EntityGraph
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // @EntityGraph 사용법 3) - Query Method + EntityGraph
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // 단순 조회용으로만 관리하기 위한 목적이기 때문에 스냅샷을 생성하지 않음 (Dirty Checking 시 스냅샷이 없기 때문에 감지 안 됨)
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // JPA - Lock 사용법 (for update)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
