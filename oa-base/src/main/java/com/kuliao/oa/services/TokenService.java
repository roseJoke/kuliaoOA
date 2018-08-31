package com.kuliao.oa.services;

import com.kuliao.oa.db.mapper.UserMapper;
import com.kuliao.oa.domain.RetEntity;
import com.kuliao.oa.domain.UserAuth;
import com.kuliao.oa.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class TokenService {
    private final static Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private UserMapper user;

    private String tokenCache;

    public String getTokenCache() {
        return tokenCache;
    }

    public void setTokenCache(String tokenCache) {
        this.tokenCache = tokenCache;
    }

    public String generateToken(String subject, String sign, Long expired) {
        if (log.isDebugEnabled()) {
            log.debug("获取token" + "，IP为" + sign + "过期时间：" + expired / 1000 + "秒");
        }
        tokenCache = "Bearer " + Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expired))
                .signWith(SignatureAlgorithm.HS512, sign)
                .compact();
        if (log.isTraceEnabled()) {
            log.trace("成功获得token" + tokenCache);
        }

        return tokenCache;
    }

    /**
     * 普通验证token，出错则抛出业务异常
     *
     * @param token
     * @param sign
     */
    public void tokenAuth(String token, String sign) {
        if (log.isDebugEnabled()) {
            log.debug("传入token:" + token + ",ip：" + sign);
        }
        if (token != null) {
            // parse the token.

            String empNo = null;
            try {
                empNo = Jwts.parser()
                        .setSigningKey(sign)
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody()
                        .getSubject();
            } catch (SignatureException e) {
                throw new BusinessException("100005", "签名非法，请检查错误");

            } catch (ExpiredJwtException e) {
                throw new BusinessException("100006", "签名过期，请重新申请");

            }
            if (empNo == null) {
                throw new BusinessException("100007", "签名无效，请确认");
            }
            UserAuth userAuth = user.selectAuthByEmpNo(empNo);

            if (userAuth == null) {
                throw new BusinessException("100001", "用户不存在");

            }else if (userAuth.getIsBlock() == 1) {
                throw new BusinessException("100002", "用户已经过期");
            }
        } else {
            throw new BusinessException("100010", "签名为空请检查");

        }
    }

    /**
     * 过滤器中的token验证，返回RetEntity包含验证结果。
     *
     * @param token
     * @param sign
     */
    public RetEntity filterTokenAuth(String token, String sign, HttpServletRequest request) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("过滤器验证token:" + token + ",ip：" + sign);
        }

        if (token != null) {
            // parse the token.

            String empNo = null;
            try {
                empNo = Jwts.parser()
                        .setSigningKey(sign)
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody()
                        .getSubject();
            } catch (SignatureException e) {

                return new RetEntity("100005", "签名非法，请检查错误");

            } catch (ExpiredJwtException e) {

                return new RetEntity("100006", "签名过期，请重新申请");

            }
            if (empNo == null) {

                return new RetEntity("100007", "签名无效，请确认");
            }
            UserAuth userAuth = user.selectAuthByEmpNo(empNo);
            log.debug("签名验证通过，用户信息："+userAuth);
            if (userAuth == null) {

                return new RetEntity("100001", "用户不存在");

            }
            if (userAuth.getIsBlock() == 1 && !request.getRequestURI().contains("lockUser")) {

                return new RetEntity("100002", "用户已经过期");
            }
        } else {

            return new RetEntity("100010", "签名为空请检查");
        }
        return null;
    }

}
