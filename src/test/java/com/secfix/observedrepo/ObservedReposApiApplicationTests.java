package com.secfix.observedrepo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.flyway.enabled=false"})
class ObservedReposApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
