package kr.pe.ihoney.webstudy;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SimpleGetServletTest {

	private MockHttpServletRequest req;
	
	@Before
	public void setUp() {
		req = new MockHttpServletRequest("GET", "/hello");
	}
	
	@Test
	public void testMock() throws ServletException, IOException {
		req.addParameter("name", "Spring");
		MockHttpServletResponse res = new MockHttpServletResponse();
		SimpleGetServlet servlet = new SimpleGetServlet();
		servlet.service(req, res);
		
		assertThat(res.getContentAsString(), is("<html><body>Hello Spring</body></html>"));
	}

}
