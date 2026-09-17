package tictactoe.web.model;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import tictactoe.domain.model.User;
import tictactoe.domain.service.UserService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AuthFilter extends GenericFilterBean {
    private final AuthorizationService authService;
    private final JwtProvider jwtProvider;
    private JwtUtil jwtUtil;
    private final UserService userService;

    public AuthFilter(AuthorizationService authService, JwtProvider jwtProvider, UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();
        System.out.println("Incoming path = [" + path + "]");
        if (path.equals("/auth/register") || path.equals("/auth/login") || path.equals("/auth/update-access")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            String accessToken = header.substring("Bearer ".length());
            if (!jwtProvider.validateAccessToken(accessToken)) {
                throw new IllegalArgumentException("Wrong access token");
            }
            Claims claims = jwtProvider.getClaims(accessToken);
            JwtAuthentication authentication = jwtUtil.createJwtAuthenticationFromClaims(claims);
            var auth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    null,
                    authentication.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
