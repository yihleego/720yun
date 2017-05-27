package com.yihleego.pano.filter;


import com.yihleego.pano.service.impl.PanoCrawlerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by YihLeego on 17-5-25.
 */
public class MainFliter implements Filter {
    private transient static Logger logger = LoggerFactory.getLogger(PanoCrawlerServiceImpl.class);


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() {


    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(30 * 60);
        String url = req.getRequestURI();
        String type = req.getHeader("X-Requested-With");
        String IP = getIpAddr(req);

        /*res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        res.setHeader("Access-Control-Allow-Credentials","true");*/


        StringBuilder requestInfo=new StringBuilder();
        requestInfo.append("ip:[");
        requestInfo.append(IP);
        requestInfo.append("]\t");
        requestInfo.append("url:[");
        requestInfo.append(url);
        requestInfo.append("]\t");
        if(type!=null) {
            requestInfo.append("type:[");
            requestInfo.append(type);
            requestInfo.append("]");
        }

        logger.info("{}",requestInfo.toString());


        boolean pass = true;
        String forwardUrl = "error.jsp";

        if (url.endsWith(".jsp")) {
            pass = false;
        }

        if (pass)
            chain.doFilter(request, response);
        else
            request.getRequestDispatcher(forwardUrl).forward(request, response);
    }

    private final static String getIpAddr(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

}
