package com.skypro.starbank.service;

import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.model.rules.RuleSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    private final String testUserId = "user-123";
    private final RuleSet testRuleSet = new RuleSet(UUID.fromString("product-1"), "Invest 500", "Investment description", List.of());

    @BeforeEach
    void setUp() {
        reset(ruleService);
    }

    @Test
    void shouldReturnRecommendations_whenUserMatchesRules() {
        when(ruleService.getAllRules()).thenReturn(List.of(testRuleSet));
        when(ruleService.checkRulesForUser(testUserId, testRuleSet)).thenReturn(true);

        RecommendationResponse response = recommendationService.getRecommendations(testUserId);

        assertNotNull(response);
        assertEquals(testUserId, response.getUserId());
        assertFalse(response.getRecommendations().isEmpty());
        assertEquals(1, response.getRecommendations().size());
        assertEquals("Invest 500", response.getRecommendations().get(0).getName());

        verify(ruleService, times(1)).getAllRules();
        verify(ruleService, times(1)).checkRulesForUser(testUserId, testRuleSet);
    }

    @Test
    void shouldReturnEmptyRecommendations_whenUserDoesNotMatchRules() {
        when(ruleService.getAllRules()).thenReturn(List.of(testRuleSet));
        when(ruleService.checkRulesForUser(testUserId, testRuleSet)).thenReturn(false);

        RecommendationResponse response = recommendationService.getRecommendations(testUserId);

        assertNotNull(response);
        assertEquals(testUserId, response.getUserId());
        assertTrue(response.getRecommendations().isEmpty());

        verify(ruleService, times(1)).getAllRules();
        verify(ruleService, times(1)).checkRulesForUser(testUserId, testRuleSet);
    }

    @Test
    void shouldReturnEmptyRecommendations_whenNoRulesExist() {
        when(ruleService.getAllRules()).thenReturn(List.of());

        RecommendationResponse response = recommendationService.getRecommendations(testUserId);

        assertNotNull(response);
        assertEquals(testUserId, response.getUserId());
        assertTrue(response.getRecommendations().isEmpty());

        verify(ruleService, times(1)).getAllRules();
        verify(ruleService, never()).checkRulesForUser(anyString(), any(RuleSet.class));
    }
}

