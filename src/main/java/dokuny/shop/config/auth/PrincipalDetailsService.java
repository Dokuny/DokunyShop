package dokuny.shop.config.auth;

import dokuny.shop.domain.Member;
import dokuny.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 시큐리티에 설정에서 loginProcessingUrl 을 통해 요청이 오면
// 자동으로 UserDetailsService 타입으로 IoC에 등록되어 있는 빈을 찾은 후 loadUserByUsername 메소드가 실행된다.
// Authentication에 UserDetails를 집어넣어주는 작업을 하는 Service
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 넘어온 username(email)으로 레파지토리에 유저가 있는지 확인.
        Member member = memberRepository.findMemberByEmail(email);

        // 있으면 만들어둔 UserDetails 구현 객체에 넣어준다.
        if (member != null) {
            // 반환된 구현 객체는 Security Session 내부의 Authentication 객체 내부의 UserDetails로 들어간다.
            return new PrincipalDetails(member);
        }

        return null;
    }
}
