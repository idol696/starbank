package com.skypro.starbank;


import com.skypro.starbank.controller.RecommendationController;
import com.skypro.starbank.service.RecommendationService;
import com.skypro.starbank.service.RuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StarBankApplicationTests {

	@Autowired
	private RuleService ruleService;

	@Autowired
	private RecommendationService recommendationService;

	@Autowired
	private RecommendationController recommendationController;

	@Test
	void contextLoads() {
		assertThat(ruleService).isNotNull();
		assertThat(recommendationService).isNotNull();
		assertThat(recommendationController).isNotNull();
	}
}

