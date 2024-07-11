package store.itpick.backend.support.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Pointcut("execution(public * kuit3.backend..*(..))) " +
            "&& !execution(public * kuit3.backend.support.logging..*(..))")
    private void allComponents() {}

    @Pointcut("execution(public * kuit3.backend.controller..*(..))")
    private void allController() {}

    @Pointcut("execution(public * kuit3.backend.service..*(..))")
    private void allService() {}

    @Pointcut("execution(public * kuit3.backend.common.argument_resolver..*(..))")
    private void allArgumentResolver() {}

    @Pointcut("execution(public * kuit3.backend.common.interceptor..*(..))")
    private void allInterceptor() {}

    @Before("allComponents()")
    public void doLog(JoinPoint joinPoint) {
        log.info("[{}]", joinPoint.getSignature().toShortString());
    }
}
