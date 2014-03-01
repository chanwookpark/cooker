package chanwook.cooker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by chanwook on 2014. 3. 1..
 */
public class CookiePrototype {

    private final Logger logger = LoggerFactory.getLogger(CookiePrototype.class);

    private final Cooker cooker;
    private final String key;

    //FIXME 상호 참조가 생겨 좋지 않으므로 더 좋은 방법 찾을 것!
    public CookiePrototype(String key, Cooker cooker) {
        this.key = key;
        this.cooker = cooker;
    }

    public CookiePrototype encoding() {
        return encoding(cooker.getCharsetName());
    }

    private CookiePrototype encoding(String charsetName) {
        Cookie cookie = cooker.getCookie(getKey());
        String originalValue = cookie.getValue();
        try {
            String encodedValue = URLEncoder.encode(originalValue, charsetName);
            cookie.setValue(encodedValue);
            if (logger.isDebugEnabled()) {
                logger.debug("Encoding cookie value: " + originalValue + " to " + encodedValue);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encoding cookie value", e);
        }
        return this;
    }

    public CookiePrototype decoding() {
        Cookie cookie = cooker.getCookie(key);
        String originalValue = cookie.getValue();
        try {
            String decodedValue = URLDecoder.decode(originalValue, cooker.getCharsetName());
            cookie.setValue(decodedValue);

            if (logger.isDebugEnabled()) {
                logger.debug("Decoding cookie value: " + originalValue + " to " + decodedValue);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to decoding cookie value", e);
        }
        return this;
    }

    public Cooker getCooker() {
        return cooker;
    }

    public String getKey() {
        return key;
    }

    public Cookie getCookie() {
        return this.cooker.getCookie(key);
    }


}
