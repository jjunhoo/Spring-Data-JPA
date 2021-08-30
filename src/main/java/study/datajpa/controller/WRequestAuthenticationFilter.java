package study.datajpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class WRequestAuthenticationFilter extends OncePerRequestFilter {

    private final WVerifier wVerifier;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        WRequestHttpServletRequestWrapper wHttpServletRequestWrapper = new WRequestHttpServletRequestWrapper(request);
        try {

            if (wVerifier.doVerify(wHttpServletRequestWrapper)) {
                wVerifier.verify(wHttpServletRequestWrapper);
            }

            filterChain.doFilter(wHttpServletRequestWrapper, response);

        } catch (ServletException | IOException e) {
            throw e;
        } catch (Exception e) {

        }
    }
}
