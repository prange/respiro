/*
 * Copyright 2015 Kantega AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kantega.respiro.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import static java.lang.String.format;
import static java.util.Base64.getDecoder;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class BasicAuthenticationFilter implements Filter {

    private final String securityRealm;
    private final PasswordChecker passwordChecker;

    public BasicAuthenticationFilter(String securityRealm, PasswordChecker passwordChecker) {
        this.securityRealm = securityRealm;
        this.passwordChecker = passwordChecker;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String auth = req.getHeader("Authorization");

        if (auth != null) {

            final String[] usernameAndPassword = new String(getDecoder().decode(auth.substring("Basic ".length()).getBytes())).split(":");
            final String username = usernameAndPassword.length>1 ? usernameAndPassword[0]: "";
            String password = usernameAndPassword.length>2 ? usernameAndPassword[1]: "";


            AuthenticationResult result = passwordChecker.checkPassword(username, password);
            if (result.isAuthenticated()) {
                filterChain.doFilter(new HttpServletRequestWrapper(req) {
                    @Override
                    public String getRemoteUser() {
                        return username;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return result.getRoles().contains(role);
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return this::getRemoteUser;
                    }
                }, servletResponse);

                return;
            }
        }


        resp.setStatus(SC_UNAUTHORIZED);
        resp.setHeader("WWW-Authenticate", format("Basic realm=\"%s\"", securityRealm));

    }

    @Override
    public void destroy() {

    }
}
