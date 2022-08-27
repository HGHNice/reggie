package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器
    public static final AntPathMatcher AntPathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest; //向下转型
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String requestURI = request.getRequestURI();
//        log.info("拦截到请求：{}", request.getRequestURL());
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg", //移动端发送短信
                "/user/login"  // 移动端登陆
        };
        boolean check = check(urls, requestURI);
        if (check){
            filterChain.doFilter(request,response);
            return;
        }
        //4-1判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            //log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            //把用户id存储到本地的threadLocal
            Long emId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(emId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2判断移动端登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            //log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            //把用户id存储到本地的threadLocal
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }

    public boolean check(String[] urls, String url) {
        for (String s : urls) {
            boolean match = AntPathMatcher.match(s, url);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
