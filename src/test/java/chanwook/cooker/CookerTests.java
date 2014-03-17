package chanwook.cooker;

import org.junit.Test;

import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by chanwook on 2014. 3. 1..
 */
public class CookerTests {

    @Test
    public void addAndGetCookie() {
        Cooker c = new Cooker();

        // create Cookie
        String key1 = "key1";
        String value1 = "value1";
        Cookie cookie = c.cooking(key1, value1).getCookie();

        assertNotNull(cookie);
        assertThat(key1, is(cookie.getName()));
        assertThat(value1, is(cookie.getValue()));
        assertTrue(-1 == cookie.getMaxAge());

        // getCookie Cookie
        cookie = c.getCookie(key1);
        assertNotNull(cookie);
        assertThat(key1, is(cookie.getName()));
        assertThat(value1, is(cookie.getValue()));

        // Update Cookie
        String value2 = "value2";
        cookie.setValue(value2);
        c.cooking(cookie);

        cookie = c.getCookie(key1);
        assertNotNull(cookie);
        assertThat(key1, is(cookie.getName()));
        assertThat(value2, is(cookie.getValue()));
    }

    @Test
    public void deleteCookie() throws Exception {
        Cooker c = new Cooker();
        String key = "key1";
        c.cooking(key, "value1");

        c.delete(key);

        Cookie cookie = c.getCookie(key);
        assertThat(0, is(cookie.getMaxAge()));
    }

    @Test
    public void hasCookie() throws Exception {
        Cooker c = new Cooker();
        String key = "key1";
        assertFalse(c.hasCookie(key));

        c.cooking(key, "value1");

        assertTrue(c.hasCookie(key));

    }

    @Test
    public void encodingDecoding() {
        Cooker c = new Cooker();
        String key = "key1";
        String value = "Hello World!";
        c.cooking(key, value).encoding();

        Cookie cookie = c.getCookie(key);
        assertThat("Hello+World%21", is(cookie.getValue()));

        CookiePrototype prototype = c.getPrototype(key);
        cookie = prototype.decoding().getCookie();
        assertThat(value, is(cookie.getValue()));
    }

    @Test
    public void encodingDecodingWithKorean() {
        Cooker c = new Cooker();
        String key = "key2";
        String value = "쿠키값이 잘 구워지나!";
        c.cooking(key, value).encoding();

        Cookie cookie = c.getCookie(key);
        assertThat("%EC%BF%A0%ED%82%A4%EA%B0%92%EC%9D%B4+%EC%9E%98+%EA%B5%AC%EC%9B%8C%EC%A7%80%EB%82%98%21",
                is(cookie.getValue()));

        CookiePrototype prototype = c.getPrototype(key);
        cookie = prototype.decoding().getCookie();
        assertThat(value, is(cookie.getValue()));
    }

    @Test
    public void delimitedCookieValue() {
        Cooker c = new Cooker();
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("subkey1", "value1");
        item.put("subkey2", "value2");
        item.put("subkey3", "value3");
        CookiePrototype prototype = c.cooking("subSet", item);

        assertThat("subSet", is(prototype.getCookie().getName()));
        assertThat("subkey2=value2~subkey1=value1~subkey3=value3", is(prototype.getCookie().getValue()));
    }

    @Test
    public void secure() {
        Cooker c = new Cooker();
        c.cooking("key1", "value1").secure();

        Cookie cookie = c.getCookie("key1");
        assertTrue(cookie.getSecure());
    }

    @Test
    public void sha256() {
        Cooker c = new Cooker();
        String key = "password";
        // default
        c.cooking(key, "12345678").sha256();

        Cookie cookie = c.getCookie(key);
        assertThat("ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f",
                is(cookie.getValue()));

        // with salt value
        c.cooking(key, "12345678").sha256("saltValue");

        cookie = c.getCookie(key);
        assertThat("d71e480d67c2c7ba50b1a8f39555e09de9ffb53b6e3482bedf6634aff5b1b068",
                is(cookie.getValue()));
    }

    @Test
    public void variousCollectionType() {
        Cooker c = new Cooker();
        c.cooking("key1", "value1");
        c.cooking("key2", "value2");
        c.cooking("key3", "value3");

        Collection<Cookie> list = c.getAllAsList();
        assertNotNull(list);
        assertThat(3, is(list.size()));

        Map<String, Cookie> map = c.getAllAsMap();
        assertNotNull(map);
        assertThat(3, is(map.size()));
        assertThat("value1", is(map.get("key1").getValue()));
        assertThat("value2", is(map.get("key2").getValue()));
        assertThat("value3", is(map.get("key3").getValue()));
    }

    @Test
    public void domain() {
        // set default domain
        String domain = "*.chanwook.com";
        Cooker c = new Cooker("UTF-8", domain);

        assertThat(domain, is(c.getDomain()));

        String key = "key1";
        c.cooking(key, "value1");

        Cookie cookie = c.getCookie(key);
        assertNotNull(cookie);
        assertThat(domain, is(cookie.getDomain()));

        // specify domain value
        String domain2 = "blog.chanwook.com";
        c.cooking(key, "value1").domain(domain2);
        cookie = c.getCookie(key);
        assertNotNull(cookie);
        assertThat(domain2, is(cookie.getDomain()));
    }

    @Test
    public void expire() {
        Cooker c = new Cooker();
        int expirySecond = 24 * 60 * 60;
        String key = "key";
        c.cooking(key, "value").expire(expirySecond);

        Cookie cookie = c.getCookie(key);
        assertThat(expirySecond, is(cookie.getMaxAge()));
    }
}
