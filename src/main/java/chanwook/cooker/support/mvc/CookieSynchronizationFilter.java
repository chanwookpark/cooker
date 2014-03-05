package chanwook.cooker.support.mvc;

import chanwook.cooker.Cooker;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cookie API인 Cooker를 생성하고, 이를 Response로 다시 동기화해주는 필터
 */
public class CookieSynchronizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Cooker cooker = initializingCookerInstance(servletRequest);

        filterChain.doFilter(servletRequest, servletResponse);

        writeCookie(cooker, (HttpServletResponse) servletResponse);

        // closing
        servletRequest.removeAttribute(HttpCookingConstant.COOKING_INSTANCE);
    }

    private Cooker initializingCookerInstance(ServletRequest servletRequest) {
        Cooker cooker = new Cooker();
        cooker.initialize((HttpServletRequest) servletRequest);
        servletRequest.setAttribute(HttpCookingConstant.COOKING_INSTANCE, cooker);
        return cooker;
    }

    protected void writeCookie(Cooker cooker, HttpServletResponse response) {
        for (Cookie cookie : cooker.getAllAsList()) {
            response.addCookie(cookie);
        }
    }

    @Override
    public void destroy() {

    }
}
