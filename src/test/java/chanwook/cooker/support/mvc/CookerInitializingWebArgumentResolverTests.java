package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        ServletWebRequest webRequest = new ServletWebRequest(req, new MockHttpServletResponse());

        req.setAttribute(HttpCookingConstant.COOKING_INSTANCE, new Cooker());
        MethodParameter param = getMockParam();

        Object returnValue = r.resolveArgument(param, new ModelAndViewContainer(), webRequest, new WebDataBinderFactory() {
            @Override
            public WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName) throws Exception {
                return null;
            }
        });

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
