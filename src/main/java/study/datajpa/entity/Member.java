package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 스펙상 파라미터가 없는 생성자가 1개는 반드시 필요 (private 은 불가 > Proxy 사용 불가)
@ToString(of = {"id", "username", "age"}) // team 은 양방향 연관관계로 매핑되어 있기 때문에 의도적으로 출력 X
// 실무에서 사용할 일 거의 없음
@NamedQuery(
        name="Member.findByUsername", // 관례
        query="select m from Member m where m.username = :username"
)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") // Team > FK
    private Team team;

    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
