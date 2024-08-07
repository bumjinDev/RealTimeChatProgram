package com.example.websocket;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class MyHandshakeInterceptor implements HandshakeInterceptor {
	
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
       System.out.println("beforeHandshake() 실행!");
     
       /* 방 번호를 httpRequest 객체로부터 받아서 session 객체 내 저장한다. 이후 해당 방 번호는 WebSocket 세션 객체 성립 이후
        * 방 번호 별 세션 객체 리스트를 생성하는 데 사용. 또한 해당 WebSocket 세션 객체가 종료 되었을 때 해당 방 번호를 기준으로 방 번호 별 세션 리스트를
        * 조회하며 해당 세션 객체가 속한 방 번호를 기준으로 방 리스트가 없을 경우 리스트 삭제한다. */
       HashMap<String, Object>  socketInfo = extractHttpSessionIdFromRequest(request);
       attributes.put("roomnumber", (String) socketInfo.get("roomnumber"));
       attributes.put("username", (String) socketInfo.get("username"));
       attributes.put("HTTP_SESSION", (HttpSession)socketInfo.get("HTTP_SESSION"));
       
       System.out.println("roomnumber : " + attributes.get("roomnumber"));
       System.out.println("username : " + attributes.get("username"));
       
       return true;
    }

    private HashMap<String, Object> extractHttpSessionIdFromRequest(ServerHttpRequest request) {
       
    	System.out.println("extractHttpSessionIdFromRequest() 호출 (session 생성 전 http 내 roomnumber 확인) ");
    	
    	ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        
    	String roomnumber = (String) servletRequest.getServletRequest().getParameter("roomnumber");
        String username = (String)  servletRequest.getServletRequest().getParameter("username");
        
        System.out.println("디버깅 - extractHttpSessionIdFromRequest : username_ " + username);
        
        HashMap<String, Object>  socketInfo = new HashMap<String, Object> ();
        socketInfo.put("roomnumber", roomnumber);
        socketInfo.put("username", username);
        socketInfo.put("HTTP_SESSION", (Object) servletRequest.getServletRequest().getSession());
        
        return socketInfo;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    	
    }
}
