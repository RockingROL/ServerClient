package org.baeldung.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.baeldung.security.ActiveUserStore;
import org.baeldung.websocket.SocketTextHandler;

@Configuration
@EnableWebSocket
@ComponentScan({ "org.baeldung.websocket" })
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private SocketTextHandler socketTextHandler;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketTextHandler, "/login");
	}
	
	@Bean
    public SocketTextHandler socketTextHandler() {
        return new SocketTextHandler();
    }
	
    @Bean
    @Nullable
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
            threadPoolScheduler.setThreadNamePrefix("SockJS-");
            threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
            threadPoolScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolScheduler;
    }

}