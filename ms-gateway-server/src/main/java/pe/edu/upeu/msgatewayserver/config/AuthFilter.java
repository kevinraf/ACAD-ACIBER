package pe.edu.upeu.msgatewayserver.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.upeu.msgatewayserver.dto.TokenDto;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClient;

    public AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null) return onError(exchange, HttpStatus.BAD_REQUEST, "Missing Authorization");

            String[] chunks = authHeader.split("\\s+");
            if (chunks.length != 2 || !chunks[0].equalsIgnoreCase("Bearer")) {
                return onError(exchange, HttpStatus.BAD_REQUEST, "Invalid Authorization format");
            }
            String token = chunks[1];

            return webClient.build()
                    .post()
                    .uri("http://ms-auth/auth/validate?token={t}", token) // serviceId correcto
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    // ✅ usar HttpStatusCode::isError en Boot 3
                    .onStatus(HttpStatusCode::isError,
                            resp -> Mono.error(new RuntimeException("Auth error " + resp.statusCode().value())))
                    .bodyToMono(TokenDto.class)
                    .flatMap(t -> chain.filter(exchange))
                    // mapear cualquier error a 401
                    .onErrorResume(ex -> onError(exchange, HttpStatus.UNAUTHORIZED, ex.getMessage()));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String msg){
        ServerHttpResponse res = exchange.getResponse();
        res.setStatusCode(status);
        res.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        return res.writeWith(Mono.just(res.bufferFactory().wrap(msg.getBytes())));
    }

    public static class Config {}
}
