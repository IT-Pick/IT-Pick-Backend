package store.itpick.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import store.itpick.backend.common.argument_resolver.GetJwtHandlerArgumentResolver;
import store.itpick.backend.common.argument_resolver.JwtAuthHandlerArgumentResolver;
import store.itpick.backend.common.interceptor.GetJwtInterceptor;
import store.itpick.backend.common.interceptor.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthenticationInterceptor;
    private final GetJwtInterceptor getJwtInterceptor;
    private final JwtAuthHandlerArgumentResolver jwtAuthHandlerArgumentResolver;
    private final GetJwtHandlerArgumentResolver getJwtHandlerArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login", "/auth/signup", "/auth/refresh","/auth/emails/**", "/rank/**","/auth/email/check","/auth/nickname/check","/favicon.ico","/keyword/**","/debate/keyword", "/test/**","/debate/trend");
                 //인터셉터 적용 범위 수정
        registry.addInterceptor(getJwtInterceptor)
                .addPathPatterns("/user/email");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthHandlerArgumentResolver);
        resolvers.add(getJwtHandlerArgumentResolver);
    }



//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        log.info("CorsMapping이 호출 되었습니다.");
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "http://localhost:5173", "https://localhost:5173", "https://itpick.netlify.app","https://itpick.vercel.app")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS")
//                .exposedHeaders("location", "Authorization")
//                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept")
//                .allowCredentials(true);
//    }

    




}