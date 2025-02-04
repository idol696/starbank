package com.skypro.starbank.controller;

import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.service.RecommendationService;
import com.skypro.starbank.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecommendationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RuleService ruleService;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    @Test
    void shouldReturnEmptyRecommendationsWhenNoMatch() throws Exception {
        when(recommendationService.getRecommendations("test-user"))
                .thenReturn(new RecommendationResponse("test-user", List.of()));

        mockMvc.perform(get("/recommendation/test-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test-user"))
                .andExpect(jsonPath("$.recommendations").isEmpty());
    }
}
