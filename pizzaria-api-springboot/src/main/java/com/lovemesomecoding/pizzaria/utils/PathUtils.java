package com.lovemesomecoding.pizzaria.utils;

public class PathUtils {

    private PathUtils() {
    }

    public static final String[] PING_URLS        = {"/ping", "/ping/", "/", "/csrf"};

    public static final String[] DEMO_URLS        = {"/demos/**"};

    public static final String[] PUBLIC_URLS      = {"/users/token/refresh", "/users/signup", "/users/signup/", "/pub/**", "/products/**", "/orders/**"};

    public static final String[] SWAGGER_DOC_URLS = {"/swagger-ui.html", "/swagger-resources", "/swagger-resources/**", "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security", "/v2/api-docs", "/webjars/**", "/webjars/springfox-swagger-ui/**", "/webjars/springfox-swagger-ui/springfox.css?v=2.8.0-SNAPSHOT",
            "/webjars/springfox-swagger-ui/swagger-ui.css?v=2.8.0-SNAPSHOT"};

    public static final String   LOGIN_URL        = "/users/login";
    public static final String   LOGOUT_URL       = "/users/logout";

}
