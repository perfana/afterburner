package io.perfana.afterburner.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
//@RequestScope
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@ToString
@Slf4j
public class MyRequestScopedThing {
    private final String user;
    private final String url;

    public MyRequestScopedThing(HttpServletRequest request) {
        this.user = request.getParameter("user");
        this.url = request.getQueryString();
        log.info("Created " + this);
    }
}
