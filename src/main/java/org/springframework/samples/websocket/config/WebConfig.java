package org.springframework.samples.websocket.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.websocket.echo.EchoWebSocketHandler;
import org.springframework.samples.websocket.echo.sockjs.EchoSockJsHandler;
import org.springframework.samples.websocket.echo.stomp.EchoStompHandler;
import org.springframework.sockjs.server.support.DefaultSockJsService;
import org.springframework.sockjs.server.support.SockJsHttpRequestHandler;
import org.springframework.stomp.StompHandler;
import org.springframework.stomp.server.StompSockJsHttpRequestHandler;
import org.springframework.stomp.support.SimpleStompHandlerMapping;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.websocket.server.support.WebSocketHttpRequestHandler;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private RootConfig rootConfig;


	@Bean
	public SimpleStompHandlerMapping stompHandlerMapping() {

		Map<String, StompHandler> urlMap = new HashMap<String, StompHandler>();
		urlMap.put("/topic/test", new EchoStompHandler());

		SimpleStompHandlerMapping hm = new SimpleStompHandlerMapping();
		hm.setUrlMap(urlMap);
		return hm;
	}

	@Bean
	public SimpleUrlHandlerMapping handlerMapping() {

		Map<String, Object> urlMap = new HashMap<String, Object>();
		urlMap.put(stompSockJsRequestHandler().getPattern(), stompSockJsRequestHandler());
		urlMap.put(sockJsRequestHandler().getPattern(), sockJsRequestHandler());
		urlMap.put("/echoWebSocket", webSocketHttpRequestHandler());

		SimpleUrlHandlerMapping hm = new SimpleUrlHandlerMapping();
		hm.setOrder(1);
		hm.setUrlMap(urlMap);

		return hm;
	}

	// STOMP over SockJS HTTP request handler

	@Bean
	public StompSockJsHttpRequestHandler stompSockJsRequestHandler() {
		return new StompSockJsHttpRequestHandler("/echoStompSockJS", sockJsService());
	}

	// SockJS HTTP request handler

	@Bean
	public SockJsHttpRequestHandler sockJsRequestHandler() {
		EchoSockJsHandler handler = new EchoSockJsHandler(this.rootConfig.echoService());
		return new SockJsHttpRequestHandler("/echoSockJS", sockJsService(), handler);
	}

	@Bean
	public DefaultSockJsService sockJsService() {
		DefaultSockJsService sockJsService = new DefaultSockJsService();
		sockJsService.setHeartbeatTime(10000);
		return sockJsService;
	}

	// WebSocket HTTP request handler

	@Bean
	public HttpRequestHandler webSocketHttpRequestHandler() {
		EchoWebSocketHandler webSocketHandler = new EchoWebSocketHandler(this.rootConfig.echoService());
		return new WebSocketHttpRequestHandler(webSocketHandler);
	}

	// Allow serving HTML files through the default Servlet

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
