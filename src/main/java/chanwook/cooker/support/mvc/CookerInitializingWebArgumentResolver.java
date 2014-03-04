package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chanwook on 2014. 3. 4..
 */
public class CookerInitializingWebArgumentResolver implements WebArgumentResolver {
    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        if (Cooker.class.equals(methodParameter.getParameterType())) {
            Cooker cooker = new Cooker();
            HttpServletRequest nativeRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();
            cooker.initialize(nativeRequest);
            return cooker;
        }
        return null;
    }
}
