package com.skypro.starbank;

import com.skypro.starbank.controller.RecommendationController;
import com.skypro.starbank.service.RecommendationService;
import com.skypro.starbank.service.RuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class StarBankApplicationTests {

	@Autowired
	private RecommendationService recommendationService;

	@Autowired
	private RecommendationController recommendationController;

	@Autowired
	private RuleService ruleService;

	@Test
	void contextLoads() {
		assertThat(recommendationService).isNotNull();
		assertThat(recommendationController).isNotNull();
		assertThat(ruleService).isNotNull();
	}
}

