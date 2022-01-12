package dokuny.shop.repository;

import dokuny.shop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    boolean existsMemberByEmail(String email);
}
