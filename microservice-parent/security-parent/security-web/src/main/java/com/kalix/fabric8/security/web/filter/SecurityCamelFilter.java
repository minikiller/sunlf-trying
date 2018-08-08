package com.kalix.fabric8.security.web.filter;

import com.kalix.fabric8.security.api.biz.ISecurityService;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class SecurityCamelFilter implements Filter {
    private List<String> exceptUrlList = new ArrayList<>();
    private ISecurityService securityService;

    public void setExceptUrlList(List<String> exceptUrlList) {
        this.exceptUrlList = exceptUrlList;
    }

    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers",
                "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,JSESSIONID,access_token");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        res.setHeader("Access-Control-Max-Age", "1209600");
        res.setHeader("Access-Control-Expose-Headers", "access_token");
        res.setHeader("Access-Control-Request-Headers", "access_token");
        res.setHeader("Expires", "-1");
        res.setHeader("Cache-Control", "no-cache");
        res.setHeader("pragma", "no-cache");
        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(200);
            return;
        }

        try {
            boolean isIncludeUrl = true;
            if (exceptUrlList != null && exceptUrlList.size() > 0) {
                for (String exceptUrl : exceptUrlList) {
                    if (Pattern.compile(exceptUrl).matcher(req.getPathInfo()).matches()) {
                        isIncludeUrl = false;
                        break;
                    }
                }
            }
            if (isIncludeUrl) {
                String accessToken = checkToken(req);
                boolean checkedLogin = securityService.checkLoginToken(accessToken);
                if (!checkedLogin) {
                    accessFailedResponse(res);
                    return;
                }
            }
        } catch(Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error trying to access security server", e);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void accessFailedResponse(HttpServletResponse res) throws IOException {
        res.addHeader("Content-Type", "application/json; charset=utf-8");
        ServletOutputStream sos = res.getOutputStream();
        String result = "{\"code\": 401,\"msg\":\"accessToken无效或已过期。\"}";
        sos.write(result.getBytes());
//        sos.write(gson.toJson(getStatus(401, "accessToken无效或已过期。")).getBytes());
        sos.flush();
        sos.close();
    }

    private String checkToken(HttpServletRequest req) {
        String accessToken = "";
/**
        if (req.getHeader("Authorization") != null) {
            accessToken = req.getHeader("Authorization");
            if (accessToken.startsWith("Bearer ")) {
                return accessToken.substring("Bearer ".length(), accessToken.length());
            } else {
                return "";
            }
        }
**/

        if (req.getHeader("AccessToken") != null) {
            accessToken = req.getHeader("AccessToken");
            return accessToken;
        }
/**
        if (req.getParameter("AccessToken") != null) {
            accessToken = req.getParameter("AccessToken");
            return accessToken;
        }

        if (req.getHeader("access_token") != null) {
            accessToken = req.getHeader("access_token");
            return accessToken;
        }

        if (req.getParameter("access_token") != null) {
            accessToken = req.getParameter("access_token");
            return accessToken;
        }

        if (HttpUtil.getCookieByName(req, "access_token") != null) {
            accessToken = HttpUtil.getCookieByName(req, "access_token").getValue();
        }
**/
        return accessToken;
    }

    @Override
    public void destroy() {
    }
}
