package study.datajpa.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom Annotation
// @Target : Annotation 이 적용될 위치 선언
// @Retention : Annotation 의 범위, 어떤 시점까지 Annotation 이 영향을 미치게 할 것인지 선언
@Target({ElementType.METHOD, ElementType.TYPE}) // Meta Annotation
@Retention(RetentionPolicy.RUNTIME) // Meta Annotation
public @interface CustomPermission {

    ApiServiceType[] value(); // ApiServiceType 값을 Value 로 사용

}
