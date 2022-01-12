package dokuny.shop.config;

import dokuny.shop.config.auth.PrincipalDetailsService;
import dokuny.shop.domain.Role;
import dokuny.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 패스워드 디코더 설정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적 자원에 대해서는 Security 적용 X -> 정적 자원들은 굳이 시큐리티 필터를 탈 필요가 없기 때문에 자원 낭비를 줄이기 위해서 설정
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

        // 이거 설정할 때 아래와 같은 식으로 설정도 가능하다.
//      web.ignoring()
//              .antMatchers("/resources/**")
//              .antMatchers("/css/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 일단 csrf는 당장 사용을 안하니 꺼둔다.
                .cors().disable() // cors 도 꺼두자. 나중에 api로 프론트 서버랑 백엔드 서버 왓다갔다할때 설정 해두는 방법이 따로 있다.
                .authorizeRequests() // 보호된 리소스 URI 설정
                    .antMatchers("/auth/**").permitAll()
                    .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                    .antMatchers("/member/**").hasRole(Role.MEMBER.name())
                .antMatchers("/").authenticated()
                .and()
                .formLogin()
                    .loginPage("/auth/login") // 로그인 페이지 경로, 앞으로 로그인은 이 경로에서 수행된다.
                    .loginProcessingUrl("/auth/login")  // 로그인 처리 경로, 로그인 form의 action과 일치시켜주어야한다.
                    .usernameParameter("email")  // form태그에서 <input name = username>이 시큐리티의 id 기본 값인데 이를 바꾸고 싶으면 이렇게 쓰면 된다.
                    .passwordParameter("pw")     // 위와 마찬가지로 password의 name을 바꾸고 싶을 때 사용
                    .defaultSuccessUrl("/",false) // 로그인 완료 후 이동할 기본 url 설정, alwaysUse를 false로 해두면 로그인을 요청하기 이전 페이지로 이동한다.
                    .failureUrl("/auth/login") // 로그인 실패 시 이동할 url
                    .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")) // 로그아웃 url 설정
                    .logoutSuccessUrl("/auth/login") // 로그아웃 성공시 돌아갈 url
                    .invalidateHttpSession(true);
    }


}
