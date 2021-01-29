package com.hqyj.util;

import com.hqyj.pojo.UserInfo;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component          //交给spring去创建
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //转换成request对象
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        //获取session对象
        HttpSession session = request.getSession();
        //获取登录用户
        UserInfo user =(UserInfo) session.getAttribute("user");

        //获取当前的url
        String url=request.getRequestURI();


        //判断用户否是否访问的是index页面
        if (url.equals("/index")){
            if (user==null){
                //回到登录页面
                request.getRequestDispatcher("/login/loginPage").forward(servletRequest,servletResponse);
            }
            else {
                //不拦截，放行
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
        else {
            //不拦截，放行
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
