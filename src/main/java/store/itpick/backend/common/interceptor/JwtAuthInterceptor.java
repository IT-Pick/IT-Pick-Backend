package store.itpick.backend.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import store.itpick.backend.common.exception.jwt.unauthorized.JwtExpiredTokenException;
import store.itpick.backend.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import store.itpick.backend.common.exception.jwt.bad_request.JwtNoTokenException;
import store.itpick.backend.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import store.itpick.backend.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    // 컨트롤러 호출전에 JWT 검증
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String accessToken = resolveAccessToken(request);
        validateAccessToken(accessToken);

        String email = jwtProvider.getPrincipal(accessToken);
        validatePayload(email);

        long userId = authService.getUserIdByEmail(email);
        request.setAttribute("userId", userId);
        return true;
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateToken(token);
        return token.substring(JWT_TOKEN_PREFIX.length());
    }

    private void validateToken(String token) {
        if (token == null) {
            throw new JwtNoTokenException(TOKEN_NOT_FOUND);
        }
        if (!token.startsWith(JWT_TOKEN_PREFIX)) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        }
    }

    private void validateAccessToken(String accessToken) {
        if (jwtProvider.isExpiredToken(accessToken)) {
            throw new JwtExpiredTokenException(EXPIRED_TOKEN);
        }
    }

    private void validatePayload(String email) {
        if (email == null) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        }
    }

}