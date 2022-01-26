package dokuny.shop.service;

import dokuny.shop.entity.Member;
import dokuny.shop.dto.MemberFormDto;
import dokuny.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Long joinMember(MemberFormDto dto) {
        dto.setPw(passwordEncoder.encode(dto.getPw()));

        Member member = dto.toEntity();

        Member saveMember = memberRepository.save(member);

        return  saveMember.getId();
    }



}
