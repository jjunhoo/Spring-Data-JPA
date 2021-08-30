package study.datajpa.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@AllArgsConstructor
@Component
public class WVerifier {
    private final ApiKeyVerifier apiKeyVerifier;
    // private final OAuth2Verifier oAuth2Verifier;

    public boolean doVerify(WRequestHttpServletRequestWrapper request) {
        return isApiKey(request) || isBearerToken(request);
    }

    public void verify(WRequestHttpServletRequestWrapper request) throws Exception {
        if (isApiKey(request)) {
            apiKeyVerifier.verify(request);
        }
    }

    private boolean isApiKey(HttpServletRequest request) {
        return isNotEmpty(request.getHeader("apiKey"));
    }

    private boolean isBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("authorization");
        if (isEmpty(authorizationHeader)) {
            return false;
        }

        if (!authorizationHeader.toLowerCase().startsWith("bearer ")) {
            return false;
        }

        return authorizationHeader.split(" ").length == 2;
    }
}
