package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static java.util.Objects.isNull;

@Component
@Aspect
@RequiredArgsConstructor
public class WPermissionProcessor {

    private final WRequestHolder wRequestHolder;

    @Pointcut("within(@study.datajpa.controller.WPermission *)")
    public void wPermissionValidate() {
        System.out.println("[wPermissionValidate] Pointcut init ");
    }

    @Around("@annotation(study.datajpa.controller.WPermission) || wPermissionValidate()")
    public Object validate(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("[validate] Around init ");
        ApiServiceType[] permissions = getPermissions(pjp);
        wRequestHolder.validate(permissions);

        return pjp.proceed();
    }

    private ApiServiceType[] getPermissions(ProceedingJoinPoint pjp) {
        System.out.println("[getPermissions] init ");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        System.out.println("[signature] : " + signature.toString());

        Method method = signature.getMethod();
        System.out.println("[method] : " + method.toString());

        WPermission wPermission = method.getAnnotation(WPermission.class);
        System.out.println("[wPermission] : " + wPermission.toString());

        if (isNull(wPermission)) {
            Class<?> declaringClass = method.getDeclaringClass();
            wPermission = declaringClass.getAnnotation(WPermission.class);
        }

        System.out.println("[wPermission.value()] :: " + wPermission.value().length);

        return wPermission.value();
    }
}
