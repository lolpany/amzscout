package com.example.amzscout;

import com.example.amzscout.controller.GoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.*;

import static com.google.common.net.HttpHeaders.X_FORWARDED_FOR;
import static java.lang.Thread.sleep;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@TestPropertySource(locations="file:C:\\Users\\user\\IdeaProjects\\amzscout\\src\\test\\resources\\test.properties")
class AmzscoutApplicationTests {

	private static final int NUMBER_OF_THREADS = 2;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void throttlingTest() throws Exception {
        ExecutorService executorService = new ThreadPoolExecutor(NUMBER_OF_THREADS, NUMBER_OF_THREADS, 1,
				TimeUnit.HOURS, new ArrayBlockingQueue<>(NUMBER_OF_THREADS));

		Future<?> future = executorService.submit(() -> {
			try {
				mockMvc.perform(get("/go")).andDo(print()).andExpect(status().isOk());
				sleep(1000);
				mockMvc.perform(get("/go")).andDo(print()).andExpect(status().isOk());
				sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		future.get();

		Future<?> alternativeFuture = executorService.submit(() -> {
			try {
				mockMvc.perform(get("/go")).andDo(print()).andExpect(status().isOk());
				mockMvc.perform(get("/go")).andDo(print()).andExpect(status().isBadGateway());
				mockMvc.perform(get("/go").header(X_FORWARDED_FOR, "0.0.0.0")).andDo(print()).andExpect(status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		alternativeFuture.get();
    }
}
