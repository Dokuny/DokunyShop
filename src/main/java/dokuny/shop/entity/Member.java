package dokuny.shop.entity;

import dokuny.shop.entity.constant.Role;
import dokuny.shop.utils.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    // 구글 로그인 시에 비밀번호는 필요 없기 때문에 nullable = true 처리
    @Column
    private String pw;

    @Column(nullable = false)
    private String name;

    @Column
    private String phoneNum;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String pw, String name, String phoneNum, Role role) {
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.phoneNum = phoneNum;
        this.role = role;
    }

    public void updateRole(Role role) {
        this.role = role;
    }


}
