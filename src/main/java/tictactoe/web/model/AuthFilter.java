package tictactoe.web.model;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AuthFilter extends GenericFilterBean {
    private final AuthorizationService authService;

    public AuthFilter(AuthorizationService authService) {
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();
        if (path.equals("/auth/register") || path.equals("/auth/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Basic ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            String base64 = header.replace("Basic ", "");
            UUID userId = authService.authorize(base64);
            var auth = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
