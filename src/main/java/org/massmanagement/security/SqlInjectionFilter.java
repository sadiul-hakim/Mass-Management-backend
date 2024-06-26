package org.massmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

@Component
class SqlInjectionFilter extends OncePerRequestFilter {

    private static final String[] SQL_REGEX = {
            "(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+CREATE(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+ALTER(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+TRUNCATE(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+RENAME(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+COMMENT(\\b)+\\s.*(.*)"
    };

    private final CopyOnWriteArrayList<Pattern> sqlValidationPattern;

    public SqlInjectionFilter() {
        sqlValidationPattern = new CopyOnWriteArrayList<>();

        for (String regex : SQL_REGEX) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            sqlValidationPattern.add(pattern);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String queryParams = URLDecoder.decode(Optional.ofNullable(request.getQueryString()).orElse(Strings.EMPTY), StandardCharsets.UTF_8);
        String pathVariable = URLDecoder.decode(Optional.ofNullable(request.getRequestURI()).orElse(Strings.EMPTY), StandardCharsets.UTF_8);

        // First cache the request then get the request body
        var requestWrapper = new CachedBodyHttpServletRequest(request);

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = requestWrapper.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String requestBody = sb.toString();

        requestBody = requestBody.replaceAll("\\r\\n|\\r|\\n", Strings.EMPTY);

        if (isSqlInjectionSafe(queryParams) && isSqlInjectionSafe(pathVariable) && isSqlInjectionSafe(requestBody)) {
            filterChain.doFilter(requestWrapper, response);
            return;
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().print("You bad boy, Sql Injection detected!");
    }

    private boolean isSqlInjectionSafe(String stringToValidate) {
        if (!StringUtils.hasText(stringToValidate)) {
            return true;
        }

        for (var pattern : sqlValidationPattern) {
            if (pattern.matcher(stringToValidate).find()) {
                return false;
            }
        }

        return true;
    }
}
