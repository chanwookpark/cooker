package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

/**
 * Created by chanwook on 2014. 3. 4..
 */
public class CookerInitializingWebArgumentResolver implements WebArgumentResolver {
    /**
     * HttpServletRequest의 쿠키 정보 Cooker 인스턴스를 생성해 반환
     *
     * @param methodParameter
     * @param nativeWebRequest
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        if (Cooker.class.equals(methodParameter.getParameterType())) {
            Object cooker =
                    nativeWebRequest.getAttribute("_cooker", RequestAttributes.SCOPE_REQUEST);
            if (cooker != null && cooker instanceof Cooker) {
                return cooker;
            }
        }
        return null;
    }
}
