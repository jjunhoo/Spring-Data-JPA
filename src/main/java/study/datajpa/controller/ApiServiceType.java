package study.datajpa.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// API 서비스 타입
@RequiredArgsConstructor
public enum ApiServiceType {

    PRODUCT("PRODUCT", "상품/딜/기초정보 관리"),
    ORDER("ORDER", "주문조회/주문확인/송장등록/클레임 조회처리"),
    ETC("ETC", "기타 관리(Q&A)"),
    SETTLE("SETTLE", "정산"),
    PROMOTION("PROMOTION", "프로모션"),
    BRANCH("BRANCH", "지점정보관리"),
    BROADCAST("BROADCAST", "방송편성표"),
    CPN("CPN", "쿠폰할인제어");

    @Getter
    private final String name;
    @Getter
    private final String description;
}
