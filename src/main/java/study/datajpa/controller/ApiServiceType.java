package study.datajpa.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// API 서비스 타입
@RequiredArgsConstructor
public enum ApiServiceType {

    PRODUCT("PRODUCT", "상품"),
    ETC("ETC", "기타");

    @Getter
    private final String name;
    @Getter
    private final String description;
}
