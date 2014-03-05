package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


/**
 * Created by chanwook on 2014. 3. 4..
 */
public class CookieSynchronizationFilterTests {

    @Test
    public void sendCooker() throws Exception {
        CookieSynchronizationFilter f = new CookieSynchronizationFilter();

        ServletRequest req = new MockHttpServletRequest();
        ServletResponse res = new MockHttpServletResponse();

        f.doFilter(req, res, new MockFilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                assertNotNull(request.getAttribute(HttpCookingConstant.COOKING_INSTANCE));
                assertTrue(request.getAttribute(HttpCookingConstant.COOKING_INSTANCE) instanceof Cooker);
            }
        });

    }

    @Test
    public void initializingCookieToCookieStore() throws Exception {
        CookieSynchronizationFilter f = new CookieSynchronizationFilter();
        MockHttpServletRequest req = new MockHttpServletRequest();
        // add request cookie value
        req.setCookies(new Cookie("k1", "v1"), new Cookie("k2", "v2"), new Cookie("k3", "v3"));
        MockHttpServletResponse res = new MockHttpServletResponse();

        f.doFilter(req, res, new MockFilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                Cooker c = (Cooker) request.getAttribute(HttpCookingConstant.COOKING_INSTANCE);

                Map<String, Cookie> cookieMap = c.getAllAsMap();
                assertTrue(3 == cookieMap.size());
                assertThat("v1", is(cookieMap.get("k1").getValue()));
                assertThat("v2", is(cookieMap.get("k2").getValue()));
                assertThat("v3", is(cookieMap.get("k3").getValue()));
            }
        });
    }
}
