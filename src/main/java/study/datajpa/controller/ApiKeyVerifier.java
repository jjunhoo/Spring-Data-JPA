package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static study.datajpa.controller.AuthType.API_KEY;

@RequiredArgsConstructor
@Component
public class ApiKeyVerifier extends Verifier{

    private final WRequestHolder wRequestHolder;
    // private final WAuthNZHolder wAuthNZHolder;
    // private final CryptoService cryptoService;

    private final static String API_KEY_HEADER = "apiKey";

    @Override
    public void verify(WRequestHttpServletRequestWrapper request) throws Exception {
        System.out.println("[verify] init ");
        // String requestToken = request.getHeader(API_KEY_HEADER);
        String decrypted;
        try {
            // decrypted = cryptoService.decryptEtc(requestToken);
            decrypted = request.getHeader(API_KEY_HEADER);
        } catch (RuntimeException e) { // green framework lib 내부 RuntimeException 처리 되어 있음.
            // log.warn("ApiKeyVerifier.verify cryptoService - message: {}", e.getMessage(), e);
            // throw new ApiKeyInvalidException(ERROR_CODE_2001);
            System.out.println("유효하지 않은 API KEY 입니다.");
        }

        /*
        String[] divided = decrypted.split("_");
        if (divided.length < 2) {
            // log.warn("ApiKeyVerifier.verify invalid structure apiKey - decrypted apiKey: {}", decrypted);
            // throw new ApiKeyInvalidException(ERROR_CODE_2001);
            System.out.println("유효하지 않은 API KEY 입니다.");
        }
        */

        /*
        String sellerId = divided[1]; // 파트너아이디 추출
        WAuthNZDto authNZ = wAuthNZHolder.getCachedWAuthNZ(sellerId);
        Seller seller = getSeller(sellerId, authNZ.getSellerType(), request);
        if (!StringUtils.equals(authNZ.getApiKey(), requestToken)) {
            // log.warn("ApiKeyVerifier.verify - unregistered apiKey: {}", requestToken);
            // throw new ApiKeyInvalidException(ERROR_CODE_2001);
            System.out.println("유효하지 않은 API KEY 입니다.");
        }*/

        // TODO : TEST
        List<ApiServiceType> permissions = new ArrayList<>();
        permissions.add(ApiServiceType.BROADCAST);
        permissions.add(ApiServiceType.ETC);

        // wRequestHolder.verifiedOf(API_KEY, requestToken, authNZ.getSellerType(), seller.getGroupId(), seller.getPartnerId(), authNZ.getPermissions());
        wRequestHolder.verifiedOf(API_KEY, "test", SellerType.PARTNER, "test", "test", permissions);
    }

}
