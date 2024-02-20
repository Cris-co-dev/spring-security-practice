package com.crisdev.api.springsecuritypractice.config.security.authorization;

import com.crisdev.api.springsecuritypractice.exception.ObjectNotFoundException;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.GrantedPermission;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.Operation;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.User;
import com.crisdev.api.springsecuritypractice.persistence.repository.security.OperationRepository;
import com.crisdev.api.springsecuritypractice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final OperationRepository operationRepository;
    private final UserService userService;

    public CustomAuthorizationManager(OperationRepository operationRepository, UserService userService) {
        this.operationRepository = operationRepository;
        this.userService = userService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {

        HttpServletRequest request = requestContext.getRequest();
        System.out.println("Url: " + request.getRequestURL());
        System.out.println("Uri: " + request.getRequestURI());

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);

        if (isPublic) {
            return new AuthorizationDecision(true);
        }

        boolean isGranted = isGranted(url, httpMethod, authentication.get());
        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {

        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<Operation> operations = obtainOperations(authentication);

        boolean isGranted = operations.stream().anyMatch(getOperationPredicate(url, httpMethod));

        System.out.println("Is granted: " + isGranted);

        return isGranted;
    }

    private List<Operation> obtainOperations(Authentication authentication) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. username: " + username));

        return user.getRole().getPermissions()
                .stream()
                .map(grantedPermission -> grantedPermission.getOperation())
                .collect(Collectors.toList());
    }

    private boolean isPublic(String url, String httpMethod) {

        List<Operation> publicAccessEndpoints = operationRepository.findByPublicAccess();

        boolean isPublic = publicAccessEndpoints.stream().anyMatch(getOperationPredicate(url, httpMethod));

        System.out.println("Is public: " + isPublic);

        return isPublic;
    }

    private String extractUrl(HttpServletRequest request) {
        //api/v1
        String contextPath = request.getContextPath();
        //Remove contextPath
        String url = request.getRequestURI();
        url = url.replace(contextPath, "");

        System.out.println("Sin context path: " + url);
        return url;
    }

    private static Predicate<Operation> getOperationPredicate(String url, String httpMethod) {
        return operation -> {
            String basePath = operation.getModule().getBasePath();

            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            System.out.println("regexPattern: " + pattern);
            Matcher matcher = pattern.matcher(url);

            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }
}
