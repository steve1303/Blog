package com.kuang.config.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 拦截器
public class HandlerInterceptor implements org.springframework.web.servlet.HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //登陆成功之后，应该会有用户的session
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            request.setAttribute("msg", "请先登陆");
            request.getRequestDispatcher("/index").forward(request, response);
            return false;
        }
        return true;
    }
}
