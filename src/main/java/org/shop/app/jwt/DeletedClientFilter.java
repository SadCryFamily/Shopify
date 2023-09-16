package org.shop.app.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DeletedClientFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.isDeleted()) {

                servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

                final Map<String, Object> body = new HashMap<>();
                body.put("error", "Access Denied");
                body.put("message", "Opps.. you can't reach the resource while account is blocked");

                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(servletResponse.getOutputStream(), body);

                log.error("ACCESS DENIED for client USERNAME: {}. Reason: {}", userDetails.getClientName(), "Account is deleted");
                return;
            }

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
