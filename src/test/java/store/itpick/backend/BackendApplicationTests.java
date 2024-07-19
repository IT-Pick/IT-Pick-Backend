package store.itpick.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

//	@Test
//	void testJsoup() throws IOException {
//		String homeUrl = "https://www.google.com/";
//		Document doc = Jsoup.connect(homeUrl).get();
//		System.out.println(doc.toString());
//	}
}
