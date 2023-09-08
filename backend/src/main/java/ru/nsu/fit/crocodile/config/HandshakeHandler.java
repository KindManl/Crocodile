package ru.nsu.fit.crocodile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.nsu.fit.crocodile.model.StompPrincipal;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class HandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String rawCookie = request.getHeaders().get("cookie").get(0);
        rawCookie = java.net.URLDecoder.decode(rawCookie, StandardCharsets.UTF_8);
        String[] rawCookieParams = rawCookie.split(";");
        for(String rawCookieNameAndValue :rawCookieParams)
        {
            String[] rawCookieNameAndValuePair = rawCookieNameAndValue.split("=");
            if(rawCookieNameAndValuePair[0].strip().equals("Login"))
                return new StompPrincipal(rawCookieNameAndValuePair[1]);
        }

        return new StompPrincipal("Unknown");
    }
}
