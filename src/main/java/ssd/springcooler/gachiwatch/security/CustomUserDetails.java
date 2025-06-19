package ssd.springcooler.gachiwatch.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import ssd.springcooler.gachiwatch.domain.Member;

public class CustomUserDetails implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 부여 필요하면 여기에 추가, 지금은 기본 ROLE_USER 리턴
        return Collections.emptyList(); // 또는 Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
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

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member getMember() {
        return member;
    }

    public Integer getMemberId() {
        return member.getMemberId() != null ? member.getMemberId() : null;
    }
}
