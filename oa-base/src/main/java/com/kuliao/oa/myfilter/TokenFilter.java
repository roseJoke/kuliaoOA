package com.kuliao.oa.myfilter;

import com.alibaba.fastjson.JSONObject;
import com.kuliao.oa.domain.RetEntity;
import com.kuliao.oa.services.TokenService;
import com.kuliao.oa.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
@WebFilter(filterName = "TokenFilter", urlPatterns = {"/*"})
public class TokenFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private TokenService tokenService;

    @Value("/${spring.application.name}")
    private String urlprefix;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * 过滤器负责验证token，验证通过后才允许访问
     *
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.debug("进入过滤器：请求地址为："+request.getRequestURI());
        String url = request.getRequestURI();
        //如果请求在排除列表中，则放行不进行过滤
        if (url.equals(urlprefix+"/user/login") || url.equals(urlprefix+"/user/auth")
                || url.equals(urlprefix+"/user/generate")  || url.equals(urlprefix+"/user/menu")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        log.debug("过滤器已拦截,请求地址为{}",request.getRequestURI());
        String token = request.getHeader("Authorization");
        String ip = TokenUtils.getIpAddress(request);
        //拿到输出流，准备写入响应对象。
        ServletOutputStream out = response.getOutputStream();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        //接受验证结果，通过输出流将JSON对象输出。
        RetEntity ret = tokenService.filterTokenAuth(token,ip,request);
        if(ret!=null){
            out.write(JSONObject.toJSONString(ret).getBytes());
            out.flush();
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
