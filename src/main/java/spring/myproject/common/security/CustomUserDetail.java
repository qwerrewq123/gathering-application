package spring.myproject.common.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.myproject.common.JwtSubject;
import spring.myproject.entity.user.User;

import java.util.Collection;
import java.util.List;
@RequiredArgsConstructor
@Getter
public class CustomUserDetail implements UserDetails {

    private final JwtSubject jwtSubject;
    private final List<GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return jwtSubject.getUsername();
    }


}
