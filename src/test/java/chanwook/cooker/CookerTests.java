package chanwook.cooker;

import org.junit.Test;

import javax.servlet.http.Cookie;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by chanwook on 2014. 3. 1..
 */
public class CookerTests {

    @Test
    public void initApi() {
        Cooker c = new Cooker();

        // create Cookie
        String key1 = "key1";
        String value1 = "value1";
        Cookie cookie = c.cooking(key1, value1).getCookie();

        assertNotNull(cookie);
        assertThat(key1, is(cookie.getName()));
        assertThat(value1, is(cookie.getValue()));

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
//        System.out.print(cookie.getValue());
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

    static class TestController {
        public void get(Cooker c) {

        }
    }
}
