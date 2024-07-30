package store.itpick.backend.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import store.itpick.backend.common.exception.jwt.bad_request.JwtNoTokenException;
import store.itpick.backend.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import store.itpick.backend.common.exception.jwt.unauthorized.JwtExpiredTokenException;
import store.itpick.backend.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import store.itpick.backend.jwt.JwtProvider;
import store.itpick.backend.service.UserService;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthRefreshInterceptor implements HandlerInterceptor {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;
    private final UserService userService;

    // 컨트롤러 호출전에 JWT 검증
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String refreshToken = resolveRefreshToken(request);
        validateRefreshToken(refreshToken);

        String email = jwtProvider.getPrincipal(refreshToken);
        validatePayload(email);

        long userId = userService.getUserIdByEmail(email);
        request.setAttribute("userId", userId);
        return true;

    }

    private String resolveRefreshToken(HttpServletRequest request) {
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

    private void validateRefreshToken(String accessToken) {
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