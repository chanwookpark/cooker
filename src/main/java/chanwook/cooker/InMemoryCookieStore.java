package chanwook.cooker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie를 HashMap으로 별도 저장하고 처리하는 클래스.
 * 이 클래스를 사용하는 경우 반드시 Http Response에 쿠키값을 동기화 해주는 로직을 별도로 수행해야 한다.
 * Created by chanwook on 2014. 3. 4..
 */
public class InMemoryCookieStore implements CookieStore {
    private final Logger logger = LoggerFactory.getLogger(InMemoryCookieStore.class);

    private Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

    @Override
    public void add(Cookie c) {
        this.cookieMap.put(c.getName(), c);
    }

    @Override
    public Collection<Cookie> getAll() {
        return cookieMap.values();
    }

    @Override
    public Map<String, Cookie> getAllAsMap() {
        return this.cookieMap;
    }

    @Override
    public Cookie get(String key) {
        return this.cookieMap.get(key);
    }

    @Override
    public void initialize(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (logger.isDebugEnabled()) {
                    logger.debug("쿠키를 쿠키저장소로 초기화합니다.(" + c.getName() + ")");
                }
                this.cookieMap.put(c.getName(), c);
            }
        }
    }
}
