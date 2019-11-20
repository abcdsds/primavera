package com.genius.primavera;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
@RestController
public class FrontRouter {

	private final FrontService frontService;
	private final static StopWatch stopWatch = new StopWatch();

	@Bean
	protected RouterFunction<ServerResponse> getOrderRoute() {
		RouterFunction<ServerResponse> responseRouterFunction = route(GET("/router/users/{userId}/orders"), req -> ok().body(frontService.findAllOrdersRx(req.pathVariable("userId")), FrontOrder.class));
		return responseRouterFunction;
	}

	@GetMapping(value = "/mvc/users/{userId}/orders")
	public FrontOrder getUserOrders(@PathVariable("userId") String userId) {
		FrontOrder frontOrder = frontService.findAllOrders(userId);
		return frontOrder;
	}
}