package dokuny.shop.config.auth;


// 시큐리티가 login 주소 요청이 오면 낚아채서 로그인을 진행시키는데
// 로그인이 완료되면 시큐리티 session(일반 session과 동일하지만 Security ContextHolder를 키값으로 세션정보를 저장시킨다)을 만들어준다.
// 여기에 들어갈 수 있는 오브젝트는 Authentication 객체
// Authentication 안에 User 정보가 들어있어야 하는데 이는 UserDetails 타입 객체로 들어가 있다.

// 정리 : Security Session => Authentication 객체 => UserDetails 객체
// UserDetails 인터페이스를 구현하면 Authentication에 집어넣을 수 있다.
// Authentication에 넣어주는 것은 UserDetailsService를 구현한 클래스를 만들어서 처리하면 된다.

import dokuny.shop.domain.Member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private Member member;  // 콤포지션

    // 생성자를 통해서 유저 정보(Member)를 넣어준다.
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // 해당 User의 권한을 리턴하는 곳.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collect = new ArrayList<>();

        // 권한을 넣어준다. 권한이 여러개라면 여러개 넣어줄 수도 있다.
        collect.add(new SimpleGrantedAuthority(member.getRole().name()));

        return collect;
    }

    //
    @Override
    public String getPassword() {
        return member.getPw();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용가능한지 물어보는데 이거를 사용해서 로그인 한지 얼마 지나면 계정이 잠기도록 하는 로직을 짤 수도 있다. false로 바꾸면된다.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
