package com.company.product_pricing_service.adapter.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    PriceControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @ParameterizedTest(name = "returns applicable price for {0}")
    @CsvSource({
            "2020-06-14T10:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
            "2020-06-14T16:00:00, 2, 25.45, 2020-06-14T15:00:00, 2020-06-14T18:30:00",
            "2020-06-14T21:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
            "2020-06-15T10:00:00, 3, 30.50, 2020-06-15T00:00:00, 2020-06-15T11:00:00",
            "2020-06-16T21:00:00, 4, 38.95, 2020-06-15T16:00:00, 2020-12-31T23:59:59"
    })
    void returnsApplicablePriceForRequestedScenarios(
            String date,
            int priceList,
            String price,
            String startDate,
            String endDate
    ) throws Exception {
        assertPrice(date, priceList, price, startDate, endDate);
    }

    @Test
    void returnsNotFoundWhenThereIsNoApplicablePrice() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2021-01-01T00:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(statusResult -> assertEquals(
                        "Price not found",
                        objectMapper.readTree(statusResult.getResponse().getContentAsString()).get("title").asText()
                ));
    }

    @Test
    void rejectsInvalidIdentifiers() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "0")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(statusResult -> assertEquals(
                        "Invalid request",
                        objectMapper.readTree(statusResult.getResponse().getContentAsString()).get("title").asText()
                ));
    }

    @Test
    void rejectsMissingParameters() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsMalformedApplicationDate() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "not-a-date")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void preservesMethodNotAllowedStatusForUnsupportedHttpMethod() throws Exception {
        mockMvc.perform(post("/api/v1/prices"))
                .andExpect(status().isMethodNotAllowed());
    }

    @ParameterizedTest(name = "includes price validity boundary {0}")
    @CsvSource({
            "2020-06-14T15:00:00, 2, 25.45",
            "2020-06-14T18:30:00, 2, 25.45",
            "2020-06-15T11:00:00, 3, 30.50",
            "2020-12-31T23:59:59, 4, 38.95"
    })
    void treatsPriceRangeBoundariesAsInclusive(String date, int priceList, String price) throws Exception {
        final String body = mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", date)
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final JsonNode response = objectMapper.readTree(body);
        assertEquals(priceList, response.get("priceList").asInt());
        assertEquals(0, new BigDecimal(price).compareTo(response.get("price").decimalValue()));
    }

    private void assertPrice(
            String date,
            int expectedPriceList,
            String expectedPrice,
            String expectedStartDate,
            String expectedEndDate
    ) throws Exception {
        final String body = mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", date)
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final JsonNode response = objectMapper.readTree(body);
        assertEquals(35455L, response.get("productId").asLong());
        assertEquals(1, response.get("brandId").asInt());
        assertEquals(expectedPriceList, response.get("priceList").asInt());
        assertEquals(expectedStartDate, response.get("startDate").asText());
        assertEquals(expectedEndDate, response.get("endDate").asText());
        assertEquals(0, new BigDecimal(expectedPrice).compareTo(response.get("price").decimalValue()));
        assertEquals("EUR", response.get("currency").asText());
    }
}
