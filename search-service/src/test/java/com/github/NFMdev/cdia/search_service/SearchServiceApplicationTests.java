package com.github.NFMdev.cdia.search_service;

import com.github.NFMdev.cdia.search_service.repository.EventDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SearchServiceApplicationTests {

	@MockBean
	private EventDocumentRepository eventDocumentRepository;

	@Test
	void contextLoads() {
	}

}
