package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import chanwook.cooker.MissingConfigurationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by chanwook on 2014. 3. 4..
 */
public class CookerInitializingWebArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Cooker.class.equals(parameter.getParameterType());
    }

    /**
     * HttpServletRequest의 쿠키 정보 Cooker 인스턴스를 생성해 반환
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object cooker =
                webRequest.getAttribute(HttpCookingConstant.COOKING_INSTANCE, RequestAttributes.SCOPE_REQUEST);
        if (cooker != null && cooker instanceof Cooker) {
            return cooker;
        }
        throw new MissingConfigurationException("Maybe not configure filter for cooking instance!");
    }
}
