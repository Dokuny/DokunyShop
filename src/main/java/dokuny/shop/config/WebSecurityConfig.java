package dokuny.shop.config;

import dokuny.shop.domain.Role;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
                .authorizeRequests() // 보호된 리소스 URI 설정
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .antMatchers("/member/**").hasRole(Role.AUTH_MEMBER.name()
                )
                ;
    }
}
