package spring.myproject.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import spring.myproject.annotation.Username;
import spring.myproject.security.CustomUserDetails;

@Component
public class UsernameMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Username.class) &&
                String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory){
        SecurityContext context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) context.getAuthentication();
        if(authentication == null) return null;
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        return username;
    }
}