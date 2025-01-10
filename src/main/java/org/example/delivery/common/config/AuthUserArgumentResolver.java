package org.example.delivery.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.delivery.auth.annotation.Auth;
import org.example.delivery.common.domain.enums.UserRole;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.AuthException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {

    boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
    boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

    if (hasAuthAnnotation != isAuthUserType) {
      throw new AuthException(ErrorCode.AUTHENTICATION_FAILED);
    }
    return hasAuthAnnotation;
  }

  @Override
  public Object resolveArgument(
      @Nullable MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory
  ) {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    Long userId = (Long) request.getAttribute("id");
    String email = (String) request.getAttribute("email");
    UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

    return new AuthUser(userId, email, userRole);
  }
}
