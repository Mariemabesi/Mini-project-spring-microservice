package tn.gl.apigateway.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tn.gl.apigateway.util.JwtUtil;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    // By implementing Ordered, we can control precedence. -1 is high priority.
    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * Global Security Filter for the API Gateway.
     * 
     * This filter intercepts every request entering the gateway to enforce security
     * policies.
     * 
     * Responsibilities:
     * 1. Allow public access to authentication endpoints (/auth/**).
     * 2. Validate JWT tokens for all other protected routes.
     * 3. Parse claims from the token (Role, Email).
     * 4. Enforce Role-Based Access Control (RBAC):
     * - Only ADMINs can perform write operations (POST/PUT/DELETE) on the Catalog
     * Service.
     * 5. Forward user identity (X-User, X-Role) to downstream microservices logic.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. Skip Auth endpoints (Public Access)
        if (path.startsWith("/auth") || path.startsWith("/actuator") || path.contains("/swagger")
                || path.contains("/api-docs")) {
            return chain.filter(exchange);
        }

        // 2. Check Authorization Header existence
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            // 3. Validate Token Signature
            jwtUtil.validateToken(token);

            // 4. Extract Claims for Authorization
            Claims claims = jwtUtil.getClaims(token);
            String role = claims.get("role", String.class); // "USER" or "ADMIN"
            String email = claims.getSubject();

            // 5. Enforce Catalog Role Policy: Only ADMIN can modify catalog
            if (path.startsWith("/catalog")) {
                HttpMethod method = request.getMethod();
                boolean isWrite = method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.DELETE;
                if (isWrite && !"ADMIN".equals(role)) {
                    return onError(exchange, "Forbidden: ADMIN only", HttpStatus.FORBIDDEN);
                }
            }

            // 6. Forward Context to Downstream Services
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User", email)
                    .header("X-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
