package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.Cookie;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by chanwook on 2014. 3. 4..
 */
public class CookerInitializingWebArgumentResolverTests {

    @Test
    public void init() throws Exception {
        CookerInitializingWebArgumentResolver r = new CookerInitializingWebArgumentResolver();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setAttribute("", new Cooker());
        ServletWebRequest webRequest = new ServletWebRequest(req, new MockHttpServletResponse());
        MethodParameter param = getMockParam();

        Object returnValue = r.resolveArgument(param, webRequest);

        assertNotNull(returnValue);
        assertTrue(returnValue instanceof Cooker);
    }
    private MethodParameter getMockParam() {
        MethodParameter param = mock(MethodParameter.class);
        when(param.getParameterType()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return Cooker.class;
            }
        });
        return param;
    }

}
