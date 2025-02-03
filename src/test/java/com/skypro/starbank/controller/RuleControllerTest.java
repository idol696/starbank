package com.skypro.starbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RuleController.class)
@ActiveProfiles("test")
class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RuleService ruleService;

    @Autowired
    private ObjectMapper objectMapper;

    private RuleSet testRuleSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testRuleSet = new RuleSet(UUID.randomUUID(), "Test Product", "Description", List.of());
    }

    @Test
    void shouldCreateRuleSet() throws Exception {
        when(ruleService.setRules(any(RuleSet.class))).thenReturn(testRuleSet);

        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRuleSet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value(testRuleSet.getProductName()))
                .andExpect(jsonPath("$.product_text").value(testRuleSet.getProductText()));

        verify(ruleService, times(1)).setRules(any(RuleSet.class));
    }

    @Test
    void shouldGetAllRules() throws Exception {
        when(ruleService.getAllRules()).thenReturn(List.of(testRuleSet));

        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].product_name").value(testRuleSet.getProductName()));

        verify(ruleService, times(1)).getAllRules();
    }

    @Test
    void shouldDeleteRuleSet() throws Exception {
        Long ruleId = 1L;
        when(ruleService.deleteRuleSet(ruleId)).thenReturn(testRuleSet);

        mockMvc.perform(delete("/rule/{ruleId}", ruleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value(testRuleSet.getProductName()));

        verify(ruleService, times(1)).deleteRuleSet(ruleId);
    }
}
