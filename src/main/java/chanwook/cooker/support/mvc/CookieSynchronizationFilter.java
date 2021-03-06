package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;
import chanwook.cooker.MissingConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cookie API인 Cooker를 생성하고, 이를 Response로 다시 동기화해주는 필터
 *
 */
//FIXME 현재 정상동작하지 않음. HTTP가 이미 flush된 이후이기 때문에..조치필요함..
public class CookieSynchronizationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(CookieSynchronizationFilter.class);

    private String defaultDomain;

    private String defaultPath;

    private String defaultEncoding;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        if (defaultEncoding == null || defaultEncoding.length() < 1) {
            throw new MissingConfigurationException("Required default encoding value at CookieSynchronizationFilter!");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cooker cooker = initializingCookerInstance(request);

        filterChain.doFilter(request, response);

        // closing
        request.removeAttribute(HttpCookingConstant.COOKING_INSTANCE);

        writeCookie(cooker, response);
    }

    protected Cooker initializingCookerInstance(HttpServletRequest servletRequest) {
        Cooker cooker = new Cooker(defaultEncoding, defaultDomain, defaultPath);
        cooker.initialize(servletRequest);
        servletRequest.setAttribute(HttpCookingConstant.COOKING_INSTANCE, cooker);
        return cooker;
    }

    protected void writeCookie(Cooker cooker, HttpServletResponse response) {
        for (Cookie oldCookie : cooker.getAllAsList()) {
            Cookie newCookie = new Cookie(oldCookie.getName(), oldCookie.getValue());
            if (oldCookie.getDomain() != null) {
                newCookie.setDomain(oldCookie.getDomain());
            } else {
                newCookie.setDomain(getDefaultDomain());
            }

            if (oldCookie.getPath() != null) {
                newCookie.setPath(oldCookie.getPath());
            } else {
                newCookie.setPath(getDefaultPath());
            }

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
    public void destroy() {
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
