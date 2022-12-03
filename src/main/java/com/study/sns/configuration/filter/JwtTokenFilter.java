package com.study.sns.configuration.filter;

import com.study.sns.model.User;
import com.study.sns.service.UserService;
import com.study.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    // 지정한 url인 경우 param check
    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String token;

        try {
            if(TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                log.info("Request with {} check the query param", request.getRequestURI());
                token = request.getQueryString().split("=")[1].trim();
            }else {
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (header == null || !header.startsWith("Bearer ")) {
                    log.error("Error occurs while getting header. header is null or invalid");
                    chain.doFilter(request, response);
                    return;
                }
                token = header.split(" ")[1].trim();
            }

            if (JwtTokenUtils.isExpired(token, key)) {
                log.error("Key is expired");
                chain.doFilter(request, response);
                return;
            }

            String userName = JwtTokenUtils.getUserName(token, key);
            User user = userService.loadUserByUserName(userName);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);

    }
}
