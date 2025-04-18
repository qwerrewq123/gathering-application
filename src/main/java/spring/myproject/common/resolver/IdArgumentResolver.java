package spring.myproject.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import spring.myproject.common.JwtSubject;
import spring.myproject.common.annotation.Id;
import spring.myproject.common.security.CustomUserDetail;

@Component
public class IdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasUsernameAnnotation = parameter.hasParameterAnnotation(Id.class);
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasUsernameAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(authentication == null) return null;
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        if(customUserDetail == null) return null;
        JwtSubject jwtSubject = customUserDetail.getJwtSubject();
        if(jwtSubject == null) return null;
        return jwtSubject.getUserId();
    }


}
