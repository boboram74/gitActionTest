package core.ghayoun.mygitai.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    @Value("${webhook.token}")
    String token;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/webhook");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ") || !auth.substring(7).trim().equals(token)) {
            res.sendError(401, "Unauthorized");
            return;
        }
        var authentication = new UsernamePasswordAuthenticationToken(
                "webhook-client", null, List.of(new SimpleGrantedAuthority("ROLE_WEBHOOK")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("통과!" + auth);
        chain.doFilter(req, res);
    }
}