package nl.stokpop.afterburner.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class AfterburnerWebMvcConfigurer implements WebMvcConfigurer {

    private static Logger log = LoggerFactory.getLogger(AfterburnerWebMvcConfigurer.class);
    
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) {
                String requestURI = request.getRequestURI();
                int status = response.getStatus();
                if (status != HttpStatus.OK.value()
                        && !ErrorPage.PATH.equals(requestURI)) {
                    String queryString = request.getQueryString() == null ? "Unknown" : request.getQueryString();
                    String reasonPhrase = HttpStatus.resolve(status) == null ? "Unknown" : HttpStatus.resolve(status).getReasonPhrase();
                    log.warn("No status [200:OK] but [{}:{}] for URI [{}] and Query String [{}]", status, reasonPhrase, requestURI, queryString);
                }
            }
        });
    }
}
