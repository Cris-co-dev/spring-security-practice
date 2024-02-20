package com.crisdev.api.springsecuritypractice.config.security.filter;

import com.crisdev.api.springsecuritypractice.exception.ObjectNotFoundException;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.User;
import com.crisdev.api.springsecuritypractice.service.UserService;
import com.crisdev.api.springsecuritypractice.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// OncePerRequestFilter extends GenericFilterBean
// El objetivo de este filtro es settear el Authentication al SecurityContextHolder, para que posteriormente,
// sea usado en el AuthorizationFilter, ya que el authentication tiene todas las authorities, el AuthorizationFilter
// debe hacer un check (AuthorizationManager) con la request.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("ENTRO EN EL FILTRO DE JWT AUTHENTICATION FILTER");

        //1./ Obtener encabezado http llamado authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {// Bearer jwt
            filterChain.doFilter(request, response);
            return; // Retorna el control a quien llamó el método actual.
        }

        //2./ Obtener jwt desde el encabezado
        String jwt = authorizationHeader.split(" ")[1];// LO separamos por el espacio

        //3./ Obtener el username desde el token y a su vez valida el formato, firma y fecha de expiracion del token.
        String username = jwtService.extractUsername(jwt);

        //4./ Settear el objeto authentication al SecurityContextHolder
        User userDetails = userService.findOneByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        //5./ Ejecutar el registro de filtros.
        filterChain.doFilter(request,response);
    }
}
