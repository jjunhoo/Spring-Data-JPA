package study.datajpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

    // JPA 스펙상 파라미터가 없는 생성자가 1개는 반드시 필요 (private 은 불가 > Proxy 사용 불가)
    protected Member() { }

    public Member(String username) {
        this.username = username;
    }
}
