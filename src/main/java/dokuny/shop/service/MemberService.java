package dokuny.shop.service;

import dokuny.shop.domain.Member;
import dokuny.shop.dto.MemberFormDto;
import dokuny.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;


    public Long joinMember(MemberFormDto dto) {
        Member member = dto.toEntity();

        Member saveMember = memberRepository.save(member);

        return  saveMember.getId();
    }



}
