package store.itpick.backend.common.argument_resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

@Slf4j
@Component
public class JwtAuthHandlerArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(PreAuthorize.class);
        log.info(Arrays.toString(parameter.getParameterAnnotations()));
        boolean hasType = long.class.isAssignableFrom(parameter.getParameterType());
        log.info("hasAnnotation={}, hasType={}, hasAnnotation && hasType={}", hasAnnotation, hasType, hasAnnotation&&hasType);
        return hasAnnotation && hasType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        log.info("userId={}", request.getAttribute("userId"));
        return request.getAttribute("userId");

    }
}