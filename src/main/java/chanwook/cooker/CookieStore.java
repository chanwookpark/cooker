package chanwook.cooker;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * Created by chanwook on 2014. 3. 4..
 */
public interface CookieStore {

    void add(Cookie c);

    Collection<Cookie> getAll();

    Map<String, Cookie> getAllAsMap();

    Cookie get(String key);

    void initialize(HttpServletRequest request);
}
