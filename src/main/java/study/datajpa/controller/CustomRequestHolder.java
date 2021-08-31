package study.datajpa.controller;

import static com.google.common.collect.Lists.newArrayList;
import static study.datajpa.controller.AuthType.API_KEY;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// TODO : 참고 - https://chung-develop.tistory.com/64
@Getter
@Component
@Scope(value = "request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class CustomRequestHolder {
    private AuthType authType;
    private String token;
    private SellerType sellerType;
    private String groupId;
    private String partnerId;
    private List<ApiServiceType> permissions;
    private boolean verified;

    // CustomRequestHolder 생성자 호출 시점에 파트너가 소유한 권한을 DB 에서 조회 -> Caching (Guava Cache)
    public CustomRequestHolder() {
        this.permissions = newArrayList();
        this.verified = false;

        // 파트너 권한 수동 추가
        List<ApiServiceType> partnerPermissions = new ArrayList<>();

        partnerPermissions.add(ApiServiceType.PRODUCT);
        partnerPermissions.add(ApiServiceType.ETC);

        verifiedOf(API_KEY, "test", SellerType.PARTNER, "test", "test", partnerPermissions);
    }

    public void validate(ApiServiceType[] permissions) {
        System.out.println("[validate > verified] : " + verified);
        if (!verified || !granted(permissions)) {
            System.out.println("[] ::: 접근 권한이 없습니다.");
        }
    }

    private boolean granted(ApiServiceType[] permissions) {
        System.out.println("[granted] init - size : " + this.permissions.size());
        for (ApiServiceType permission : permissions) {
            // * permission : 파라미터로 넘어온 해당 Controller (Service) 의 어노테이션 Service Type ('PRODUCT') - @CustomPermission(ApiServiceType.PRODUCT)
            // * this.permission : 해당 파트너가 소유한 사용 가능한 서비스 권한 (ApiServiceType List)
            // code : 해당 파트너가 소유한 권한중에 @CustomPermission(ApiServiceType.PRODUCT) 이 있다면 true
            if (this.permissions.contains(permission)) {
                System.out.println("[granted] -> true -> " + permission);
                return true;
            }
        }
        System.out.println("[granted] - false");

        return false;
    }

    public void verifiedOf(AuthType authType, String token, SellerType sellerType, String groupId, String partnerId, List<ApiServiceType> permissions) {
        System.out.println("[verifiedOf > constructor] init ");
        this.verified = true;
        this.authType = authType;
        this.token = token;
        this.sellerType = sellerType;
        this.groupId = groupId;
        this.partnerId = partnerId;
        this.permissions = permissions;
    }
}
