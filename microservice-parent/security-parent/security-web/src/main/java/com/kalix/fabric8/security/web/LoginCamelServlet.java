package com.kalix.fabric8.security.web;

import com.kalix.fabric8.security.api.biz.ISecurityService;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class LoginCamelServlet extends CamelHttpTransportServlet {
    private ISecurityService securityService;

    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.log.trace("Service: {}", request);
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Map<String, Object> loginInfo = securityService.doLogin(username, password);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (loginInfo != null) {
            String jwtToken = (String)loginInfo.get("jwtToken");
            Cookie cookieRealName = new Cookie("access_token", jwtToken);
            response.addCookie(cookieRealName);
            response.setHeader("Authorization", "Bearer " + jwtToken);
            out.println("{\"success\":true," +
                    "\"message\":\"login success\"," +
                    "\"user\":{\"name\":\"" + loginInfo.get("name") +
                    "\",\"id\":\"" + loginInfo.get("user_id") + "\"},\"access_token\":\"" + jwtToken + "\"}");
        } else {
            out.println("{\"success\":false," +
                    "\"message\":\"login failed\"," +
                    "\"user\":{\"name\":\"" + username + "\"}}");
        }

        out.flush();
        out.close();
    }
}
