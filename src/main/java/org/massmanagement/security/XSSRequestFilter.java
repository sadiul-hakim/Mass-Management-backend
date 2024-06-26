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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
class XSSRequestFilter extends OncePerRequestFilter {
    private static final String[] XSS_REGEX = {
            "onclick|onkeypress|onkeydown|onkeyup|onerror|onchange|onmouseover|onmouseout|onblur|onselect|onfocus|onerror",
            "<\s*script\b[^>]*>(.*?)<\s*/script\b[^>]*>", "script\s+src\s*=", "<\s*script\b[^>]*>",
            "<\s*/script\b[^>]*>", "javascript.*:"};

    private final List<Pattern> xssValidationPattern;

    public XSSRequestFilter() {
        xssValidationPattern = new ArrayList<>();

        for (String xss : XSS_REGEX) {
            var pattern = Pattern.compile(xss);
            xssValidationPattern.add(pattern);
        }
    }

    private boolean isXssSafe(String stringToValidate) {
        if (!StringUtils.hasText(stringToValidate)) {
            return true;
        }

        for (var pattern : xssValidationPattern) {
            if (pattern.matcher(stringToValidate).find()) {
                return false;
            }
        }

        return true;
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

        if (isXssSafe(queryParams) && isXssSafe(pathVariable) && isXssSafe(requestBody)) {
            filterChain.doFilter(requestWrapper, response);
            return;
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().print("You bad boy, XSS detected!");
    }
}
