package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by chanwook on 2014. 3. 4..
 */
public class CookieSynchronizationFilterTests {

    @Test
    public void sendCooker() throws Exception {
        CookieSynchronizationFilter f = new CookieSynchronizationFilter();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        f.doFilter(req, res, filterChain);

        ArgumentCaptor<HttpServletRequest> arg = ArgumentCaptor.forClass(HttpServletRequest.class);
        verify(filterChain).doFilter(arg.capture(), res);

        assertNotNull(arg.getValue().getAttribute(HttpCookingConstant.COOKING_INSTANCE));
        assertTrue(arg.getValue().getAttribute(HttpCookingConstant.COOKING_INSTANCE) instanceof Cooker);
    }

    @Test
    public void initializingCookieToCookieStore() throws Exception {
        CookieSynchronizationFilter f = new CookieSynchronizationFilter();
        MockHttpServletRequest req = new MockHttpServletRequest();
        // add request cookie value
        req.setCookies(new Cookie("k1", "v1"), new Cookie("k2", "v2"), new Cookie("k3", "v3"));
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        f.doFilter(req, res, filterChain);

        ArgumentCaptor<HttpServletRequest> arg = ArgumentCaptor.forClass(HttpServletRequest.class);
        verify(filterChain).doFilter(arg.capture(), res);

        Cooker c = (Cooker) arg.getValue().getAttribute(HttpCookingConstant.COOKING_INSTANCE);

        Map<String, Cookie> cookieMap = c.getAllAsMap();
        assertTrue(3 == cookieMap.size());
        assertThat("v1", is(cookieMap.get("k1").getValue()));
        assertThat("v2", is(cookieMap.get("k2").getValue()));
        assertThat("v3", is(cookieMap.get("k3").getValue()));
    }
}
