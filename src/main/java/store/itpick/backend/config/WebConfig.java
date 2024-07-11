package store.itpick.backend.config;

import kuit3.backend.common.argument_resolver.JwtAuthHandlerArgumentResolver;
import kuit3.backend.common.interceptor.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthenticationInterceptor;
    private final JwtAuthHandlerArgumentResolver jwtAuthHandlerArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .order(1)
                .addPathPatterns("/auth/test","/users/**")
                .excludePathPatterns("/users");
                 //인터셉터 적용 범위 수정
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthHandlerArgumentResolver);
    }
}