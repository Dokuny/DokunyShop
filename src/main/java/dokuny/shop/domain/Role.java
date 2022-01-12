package dokuny.shop.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role  {

    UNAUTH_MEMBER("ROLE_AUTH","미인증 사용자"),
    AUTH_MEMBER("ROLE_UNAUTH","인증 사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;
}
