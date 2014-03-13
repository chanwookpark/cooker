package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chanwook on 2014. 3. 14..
 */
public class CookieSynchronizationInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(CookieSynchronizationInterceptor.class);

    private String defaultDomain;

    private String defaultPath;

    private String defaultEncoding;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cooker cooker = initializingCookerInstance(request);
        if (cooker != null) {
            return true;
        }
        return false;
    }

    protected Cooker initializingCookerInstance(HttpServletRequest servletRequest) {
        Cooker cooker = new Cooker(defaultEncoding, defaultDomain, defaultPath);
        cooker.initialize(servletRequest);
        servletRequest.setAttribute(HttpCookingConstant.COOKING_INSTANCE, cooker);
        return cooker;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object instance = request.getAttribute(HttpCookingConstant.COOKING_INSTANCE);
        if (instance != null && instance instanceof Cooker) {
            writeCookie((Cooker) instance, response);
        }
    }

    protected void writeCookie(Cooker cooker, HttpServletResponse response) {
        for (Cookie oldCookie : cooker.getAllAsList()) {
            Cookie newCookie = new Cookie(oldCookie.getName(), oldCookie.getValue());
            if (oldCookie.getDomain() != null) {
                newCookie.setDomain(oldCookie.getDomain());
            } else if (StringUtils.hasText(getDefaultDomain())) {
                newCookie.setDomain(getDefaultDomain());
            } //else { domain = null }

            if (oldCookie.getPath() != null) {
                newCookie.setPath(oldCookie.getPath());
            } else if (StringUtils.hasText(getDefaultPath())) {
                newCookie.setPath(getDefaultPath());
            } //else { path = null }

            if (oldCookie.getComment() != null) {
                newCookie.setComment(oldCookie.getComment());
            } else {
                newCookie.setComment("");
            }
            newCookie.setMaxAge(oldCookie.getMaxAge());
            newCookie.setSecure(oldCookie.getSecure());
            newCookie.setHttpOnly(oldCookie.isHttpOnly());
            newCookie.setVersion(oldCookie.getVersion());

            if (logger.isDebugEnabled()) {
                logger.debug("Write cookie to HttpResponse(" +
                        "name: " + newCookie.getName() + ", " +
                        "value: " + newCookie.getValue() + ", " +
                        "version: " + newCookie.getVersion() + ", " +
                        "maxAge(expire): " + newCookie.getMaxAge() + ", " +
                        "domain: " + newCookie.getDomain() + ", " +
                        "path: " + newCookie.getPath() + ", " +
                        "secure: " + newCookie.getSecure() + ", " +
                        "comment: " + newCookie.getComment() +
                        ")");
            }
            response.addCookie(newCookie);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //TODO What do???
        Object instance = request.getAttribute(HttpCookingConstant.COOKING_INSTANCE);
        if (instance != null) {
            request.removeAttribute(HttpCookingConstant.COOKING_INSTANCE);
        }
    }

    public String getDefaultDomain() {
        return defaultDomain;
    }

    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }
}
