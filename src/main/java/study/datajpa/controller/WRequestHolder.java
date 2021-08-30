package study.datajpa.controller;

import static com.google.common.collect.Lists.newArrayList;
import static study.datajpa.controller.AuthType.API_KEY;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@Scope(value = "request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class WRequestHolder {
    // TODO : ApiKeyVarifier.java -> wRequestHolder.verifiedOf(API_KEY, requestToken, authNZ.getSellerType(), seller.getGroupId(), seller.getPartnerId(), authNZ.getPermissions()); 확인
    private AuthType authType;
    private String token;
    private SellerType sellerType;
    private String groupId;
    private String partnerId;
    private List<ApiServiceType> permissions;
    private boolean verified;

    public WRequestHolder() {
        this.permissions = newArrayList();
        this.verified = false;

        // TODO : TEST
        List<ApiServiceType> innerPermissions = new ArrayList<>();

        innerPermissions.add(ApiServiceType.ETC);
        innerPermissions.add(ApiServiceType.BROADCAST);

        verifiedOf(API_KEY, "test", SellerType.PARTNER, "test", "test", innerPermissions);
    }

    public void validate(ApiServiceType[] permissions) {
        System.out.println("[validate > verified] : " + verified);
        if (!verified || !granted(permissions)) {
        // if (!granted(permissions)) {
            // throw new UnauthorizedException(ERROR_CODE_2007, ERROR_CODE_2007.getDescription());
            System.out.println("[] ::: 접근 권한이 없습니다.");
        }
    }

    private boolean granted(ApiServiceType[] permissions) {
        System.out.println("[granted] init - size : " + this.permissions.size());
        for (ApiServiceType permission : permissions) {
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
