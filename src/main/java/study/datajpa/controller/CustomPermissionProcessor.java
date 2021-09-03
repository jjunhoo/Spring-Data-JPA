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

@Aspect // Spring AOP - 횡단 관심사 클래스 선언
@Component // @Aspect를 선언해도 자동으로 빈은 등록되지 않기 때문에
@RequiredArgsConstructor
public class CustomPermissionProcessor {

    private final CustomRequestHolder customRequestHolder;

    // 2. Pointcut - Controller Layer > @CustomPermission Annotation
    // - @CustomPermission : 어노테이션이 적용된 Controller 레이어 포인트컷 지정
    // - within : Class 경로
    @Pointcut("within(@study.datajpa.controller.CustomPermission *)")
    public void customPermissionValidate() { }

    // 2. Around - @CustomPermission Annotation > ApiServiceType 확인
    // - @CustomPermission 어노테이션 또는 customPermissionValidate() 메소드에 적용
    @Around("@annotation(study.datajpa.controller.CustomPermission) || customPermissionValidate()")
    public Object validate(ProceedingJoinPoint pjp) throws Throwable {
        ApiServiceType[] permissions = getPermissions(pjp); // 해당 어노테이션 Value 값 (Service Type) 추출
        customRequestHolder.validate(permissions); // API 서비스 권한 validate

        return pjp.proceed(); // 타겟 메소드를 실행 (@CustomPermission 어노테이션과 customPermissionValidate() 메소드에 대하여 위 코드들을 실행)
    }

    // 해당 Controller 레이어에 적용한 @CustomPermission 어노테이션의 값 추출 (서비스 타입 값)
    private ApiServiceType[] getPermissions(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature(); // Reflection
        Method method = signature.getMethod();
        CustomPermission customPermission = method.getAnnotation(CustomPermission.class); // Annotation 정보 취득

        if (isNull(customPermission)) {
            Class<?> declaringClass = method.getDeclaringClass();
            customPermission = declaringClass.getAnnotation(CustomPermission.class);
        }

        return customPermission.value(); // Request 가 요청된 Controller 에 선언된 어노테이션 값 취득
    }
}
