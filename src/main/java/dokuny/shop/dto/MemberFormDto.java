package dokuny.shop.dto;

import dokuny.shop.domain.Member;

import dokuny.shop.domain.Role;
import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberFormDto {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}",
                message = "비밀번호는 영문자와 숫자,특수기호가 적어도 1개 이상 포함된 8~20자 비밀번호여야 합니다.")
    private String pw;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}",
                message = "비밀번호는 영문자와 숫자,특수기호가 적어도 1개 이상 포함된 8~20자 비밀번호여야 합니다.")
    private String pwCheck;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNum;

    @Size(min = 1, max=10,message = "이름을 1~10자 사이로 입력해주세요.")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;


    public Member toEntity() {
        return Member.builder()
                .email(email)
                .pw(pw)
                .phoneNum(phoneNum)
                .name(name)
                .role(Role.MEMBER)
                .build();
    }


}
