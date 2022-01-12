package dokuny.shop.utils;

import dokuny.shop.dto.MemberFormDto;
import dokuny.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class JoinFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MemberFormDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberFormDto dto = (MemberFormDto) target;
        if (memberRepository.existsMemberByEmail(dto.getEmail())) {
            errors.rejectValue("email","invalid.email","이미 사용중인 이메일 입니다.");
        }

        if (!dto.getPw().equals(dto.getPwCheck())) {
            errors.rejectValue("pwCheck","invalid","비밀번호가 일치하지 않습니다.");
        }
    }
}
