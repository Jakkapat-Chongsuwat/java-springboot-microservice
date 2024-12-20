package com.jakkapat.api_gateway.routes;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

        @Value("${product.service.url}")
        private String productServiceUrl;

        @Value("${order.service.url}")
        private String orderServiceUrl;

        @Value("${inventory.service.url}")
        private String inventoryServiceUrl;

        @Bean
        public RouterFunction<ServerResponse> productServiceRoute() {
                return GatewayRouterFunctions.route("product_service")
                                .route(RequestPredicates.path("/api/v1/product/**"), HandlerFunctions.http(
                                                productServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                                                URI.create("forward:/fallback")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("product_service_swagger")
                                .route(RequestPredicates.path("/aggregate/product-service/v1/api-docs"),
                                                HandlerFunctions.http(productServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                                "productServiceSwaggerCircuitBreaker",
                                                URI.create("forward:/fallback")))
                                .filter(setPath("/v1/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> orderServiceRoute() {
                return GatewayRouterFunctions.route("order_service")
                                .route(RequestPredicates.path("/api/v1/order/**"), HandlerFunctions.http(
                                                orderServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                                                URI.create("forward:/fallback")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("order_service_swagger")
                                .route(RequestPredicates.path("/aggregate/order-service/v1/api-docs"),
                                                HandlerFunctions.http(orderServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                                "orderServiceSwaggerCircuitBreaker",
                                                URI.create("forward:/fallback")))
                                .filter(setPath("/v1/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> inventoryServiceRoute() {
                return GatewayRouterFunctions.route("inventory_service")
                                .route(RequestPredicates.path("/api/v1/inventory/**"), HandlerFunctions.http(
                                                inventoryServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                                                URI.create("forward:/fallback")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("inventory_service_swagger")
                                .route(RequestPredicates.path("/aggregate/inventory-service/v1/api-docs"),
                                                HandlerFunctions.http(inventoryServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                                "inventoryServiceSwaggerCircuitBreaker",
                                                URI.create("forward:/fallback")))
                                .filter(setPath("/v1/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> fallbackRoute() {
                return route("fallbackRoute")
                                .GET("/fallback", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                                .body("Service is temporarily unavailable. Please try again later."))
                                .build();
        }
}
