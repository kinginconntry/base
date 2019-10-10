package com.needto.web.session;

import com.google.common.collect.Lists;
import com.needto.web.data.Constant;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * session 基于客户端cookie和header的实现机制的识别机制
 * @author Administrator
 */
public class HeaderCookieHttpSessionIdResolver implements HttpSessionIdResolver {


    private static final String WRITTEN_SESSION_ID_ATTR = HeaderCookieHttpSessionIdResolver.class
            .getName().concat(".WRITTEN_SESSION_ID_ATTR");

    private final String key;

    private CookieSerializer cookieSerializer;

    /**
     * token key
     * @return
     */
    public static HeaderCookieHttpSessionIdResolver token(CookieSerializer cookieSerializer){
        return new HeaderCookieHttpSessionIdResolver(Constant.TOKEN_KEY, cookieSerializer);
    }

    public static HeaderCookieHttpSessionIdResolver token(){
        return token(null);
    }

    public HeaderCookieHttpSessionIdResolver(String key) {
        this(key, null);
    }

    public HeaderCookieHttpSessionIdResolver(String key, CookieSerializer cookieSerializer) {
        if (key == null) {
            throw new IllegalArgumentException("headerName cannot be null");
        }
        this.key = key;
        this.cookieSerializer = cookieSerializer;
        if(cookieSerializer == null){
            this.cookieSerializer = new TokenCookieSeralizer(key);
        }
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        String value = request.getParameter(key);
        if(!StringUtils.isEmpty(value)){
            return Lists.newArrayList(value);
        }

        List<String> values = cookieSerializer.readCookieValues(request);
        if(!CollectionUtils.isEmpty(values)){
            return values;
        }

        Enumeration<String> fingerPrintEum = request.getHeaders(key);
        values = new ArrayList<>();
        while (fingerPrintEum.hasMoreElements()){
            values.add(fingerPrintEum.nextElement());
        }
        return values;
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        response.setHeader(this.key, sessionId);
        if (sessionId.equals(request.getAttribute(WRITTEN_SESSION_ID_ATTR))) {
            return;
        }
        request.setAttribute(WRITTEN_SESSION_ID_ATTR, sessionId);
        this.cookieSerializer
                .writeCookieValue(new CookieSerializer.CookieValue(request, response, sessionId));
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(this.key, "");
        this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, ""));
    }
}
