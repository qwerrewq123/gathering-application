package spring.myproject.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.provider.JwtProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try{
            String token = parseBearerToken(request);

            if(token == null){
                filterChain.doFilter(request,response);
                return;

            }

            String userId = jwtProvider.validate(token);

            if(userId==null){
                filterChain.doFilter(request,response);
                return;

            }

            String role = "ROLE_USER";


            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId,null, authorities);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);

            SecurityContextHolder.setContext(securityContext);


        }catch (Exception exception){
            exception.printStackTrace();

        }

        filterChain.doFilter(request,response);
    }


    private String parseBearerToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        boolean hasAuthorization = StringUtils.hasText(authorization);

        if(!hasAuthorization){
            return null;
        }

        boolean isBearer = authorization.startsWith("Bearer ");
        if(!isBearer){
            return null;

        }

        String token = authorization.substring(7);

        return token;
    }
}