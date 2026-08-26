package com.cepein.sdd.config;

import com.cepein.sdd.web.exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RbacInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("DELETE".equalsIgnoreCase(request.getMethod())) {
            String roleHeader = request.getHeader("X-User-Role");
            if (!"ADMIN".equalsIgnoreCase(roleHeader)) {
                throw new AccessDeniedException("Apenas usuários com perfil 'Administrador' possuem permissão para excluir registros");
            }
        }
        return true;
    }
}
