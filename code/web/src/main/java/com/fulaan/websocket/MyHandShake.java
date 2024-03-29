package com.fulaan.websocket;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by scott on 2017/11/2.
 */
public class MyHandShake implements HandshakeInterceptor {


    protected static final Logger LOG = Logger.getLogger(MyHandShake.class);


    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {

    }

    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        try {
            if (request instanceof ServletServerHttpRequest) {
                ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                // 标记用户
                String tokenId = servletRequest.getServletRequest().getParameter("tokenId");
                LOG.warn("servlet request tokenId is :" + tokenId);
                if (tokenId != null) {
                    attributes.put("tokenId", tokenId);
                } else {
                    return true;
                }
            }
            LOG.info("websocket handshake success");
            return true;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LOG.warn(sw.toString());
            LOG.warn("websocket 的handshake出现异常");
            return false;
        }
    }

}
