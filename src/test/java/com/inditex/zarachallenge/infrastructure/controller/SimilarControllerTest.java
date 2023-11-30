package com.inditex.zarachallenge.infrastructure.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.netty.MockServer;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
class SimilarControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	private static String URL = "/product/{id}/similar";

	@Autowired
	DataSource dataSource;

	MockServerClient mockServerClient;

	@BeforeEach
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext)
				.alwaysDo(MockMvcResultHandlers.print())
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.build();
		mockServerClient = new MockServerClient("localhost", 3000);
		mockServerClient.when(HttpRequest.request().withMethod("GET").withPath("/product/88888/similarids"))
				.respond(HttpResponse.response().withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@AfterAll
	void clear() throws SQLException {
		ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("clear.sql"));
	}


	@Test
	void testInput9() throws Exception {
		mockMvc.perform(get(URL, 9).characterEncoding("utf-8"))
				.andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(jsonPath("$.[0].id", is("11")))
				.andExpect(jsonPath("$.[0].name", is("Cotton T-shirt")))
				.andExpect(jsonPath("$.[0].price", is(39.99)))
				.andExpect(jsonPath("$.[0].availability", is(false)))
				.andExpect(jsonPath("$.[1].id", is("15")))
				.andExpect(jsonPath("$.[1].name", is("Button-up shirt")))
				.andExpect(jsonPath("$.[1].price", is(49.99)))
				.andExpect(jsonPath("$.[1].availability", is(true)))
				.andExpect(jsonPath("$.[2].id", is("19")))
				.andExpect(jsonPath("$.[2].name", is("Linen pants")))
				.andExpect(jsonPath("$.[2].price", is(29.99)))
				.andExpect(jsonPath("$.[2].availability", is(true)))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testInput12() throws Exception {
		mockMvc.perform(get(URL, 12))
				.andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(jsonPath("$.[0].id", is("20")))
				.andExpect(jsonPath("$.[0].name", is("Polo shirt")))
				.andExpect(jsonPath("$.[0].price", is(24.99)))
				.andExpect(jsonPath("$.[0].availability", is(false)))
				.andExpect(jsonPath("$.[1].id", is("18")))
				.andExpect(jsonPath("$.[1].name", is("Chinos")))
				.andExpect(jsonPath("$.[1].price", is(59.99)))
				.andExpect(jsonPath("$.[1].availability", is(true)))
				.andExpect(jsonPath("$.[2].id", is("19")))
				.andExpect(jsonPath("$.[2].name", is("Linen pants")))
				.andExpect(jsonPath("$.[2].price", is(29.99)))
				.andExpect(jsonPath("$.[2].availability", is(true)))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testInputNotFound() throws Exception {
		mockMvc.perform(get(URL, 99999))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
				.andExpect(jsonPath("$.status").value("NOT_FOUND"))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testInputUnprocessableRequest() throws Exception {
		mockMvc.perform(get(URL, "fakeUri"))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
				.andExpect(jsonPath("$.errorCode").value("UNPROCESSABLE_REQUEST"))
				.andExpect(jsonPath("$.status").value("UNPROCESSABLE_ENTITY"))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testInputUnexpectedError() throws Exception {
		mockMvc.perform(get(URL, 88888))
				.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
				.andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
				.andReturn().getResponse().getContentAsString();

		mockServerClient.verify(
				HttpRequest.request()
					.withMethod("GET")
					.withPath("/product/88888/similarids"),
				VerificationTimes.exactly(4)
		);
	}

}