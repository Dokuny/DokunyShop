package dokuny.shop.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role  {

    MEMBER("ROLE_MEMBER","인증 사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;
}
