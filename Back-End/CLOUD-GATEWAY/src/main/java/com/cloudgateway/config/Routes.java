package com.cloudgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.RetryFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {

    @Value("${hotel.service.url}")
    private String hotelService;

    @Value("${booking.service.url}")
    private String bookingService;

    @Value("${user.service.url}")
    private String userService;


    @Bean
    public RouterFunction<ServerResponse> hotelServiceRoute() {
        return GatewayRouterFunctions.route("hotel_service")
                .route(RequestPredicates.path("/hotel/**"), HandlerFunctions.http(hotelService))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("hotel-id",
                        URI.create("forward:/fallbackRoute")))
                .filter(RetryFilterFunctions.retry(3))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> bookingServiceRoute(){
        return GatewayRouterFunctions.route("booking_service")
                .route(RequestPredicates.path("/bookings/**"), HandlerFunctions.http(bookingService))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("booking-id",
                        URI.create("forward:/fallbackRoute")))
                .filter(RetryFilterFunctions.retry(3))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> userServiceRoute(){
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/user/**"), HandlerFunctions.http(userService))
                .filter(RetryFilterFunctions.retry(3))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("user-id",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }



   @Bean
    public RouterFunction<ServerResponse> fallbackRoute(){
        return route("fallbackRoute-id")
                .route(RequestPredicates.path("/fallbackRoute"),
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Service Unavaiable, please try later"))
                .build();
   }



}
