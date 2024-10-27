package com.jakkapat.order_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.jakkapat.order_service.stubs.InventoryClientStub;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@Testcontainers
class OrderServiceApplicationTests {

	@SuppressWarnings("resource")
	@Container
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0")
			.withDatabaseName("order_service")
			.withUsername("test")
			.withPassword("test");

	@LocalServerPort
	private Integer port;

	@DynamicPropertySource
	@SuppressWarnings("unused")
	static void setDataSourceProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@BeforeEach
	@SuppressWarnings("unused")
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
				{
				    "skuCode": "iphone_15",
				    "price": 1000,
				    "quantity": 1
				}
				""";

		InventoryClientStub.stubInventoryCall("iphone_15", 1);

		var response = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/v1/order")
				.then()
				.log().all()
				.extract().response();

		System.out.println("Response body: " + response.body().asString());

		assertEquals(200, response.statusCode());
		assertEquals("Order Placed Successfully", response.body().asString());
	}

	@Test
	void shouldFailOrderWhenProductIsNotInStock() {
		String submitOrderJson = """
				{
				    "skuCode": "iphone_15",
				    "price": 1000,
				    "quantity": 1000
				}
				""";
		InventoryClientStub.stubInventoryCall("iphone_15", 1000);

		RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/v1/order")
				.then()
				.log().all()
				.statusCode(500);
	}
}
