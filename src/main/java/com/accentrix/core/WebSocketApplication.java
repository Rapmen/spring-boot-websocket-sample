package com.accentrix.core;

import com.accentrix.chat.ChatEndPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.accentrix.client.GreetingService;
import com.accentrix.client.SimpleGreetingService;
import com.accentrix.echo.DefaultEchoService;
import com.accentrix.echo.EchoService;
import com.accentrix.echo.EchoWebSocketHandler;
import com.accentrix.reverse.ReverseWebSocketEndpoint;
import com.accentrix.snake.SnakeWebSocketHandler;

@Configuration
@EnableAutoConfiguration
@EnableWebSocket
public class WebSocketApplication extends SpringBootServletInitializer implements WebSocketConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(WebSocketApplication.class, args);
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(echoWebSocketHandler(), "/echo").withSockJS();
		registry.addHandler(snakeWebSocketHandler(), "/snake").withSockJS();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebSocketApplication.class);
	}

	@Bean
	public EchoService echoService() {
		return new DefaultEchoService("Did you say \"%s\"?");
	}

	@Bean
	public GreetingService greetingService() {
		return new SimpleGreetingService();
	}

	@Bean
	public WebSocketHandler echoWebSocketHandler() {
		return new EchoWebSocketHandler(echoService());
	}

	@Bean
	public WebSocketHandler snakeWebSocketHandler() {
		return new PerConnectionWebSocketHandler(SnakeWebSocketHandler.class);
	}

	@Bean
	public ReverseWebSocketEndpoint reverseWebSocketEndpoint() {
		return new ReverseWebSocketEndpoint();
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public ChatEndPoint chatEndPoint() {
		return new ChatEndPoint();
	}

}
