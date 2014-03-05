package chanwook.cooker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    /**
     * Cookie 삭제. maxAge = 0 으로 처리.
     *
     */
    public void delete() {
        expire(0);
    }

    public CookiePrototype encoding() {
        return encoding(cooker.getCharsetName());
    }

    /**
     * URLEncoder를 사용해 쿠키값 인코딩
     *
     * @param charsetName
     * @return
     */
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

    /**
     * URLDecoder를 사용해 쿠키값 디코딩.
     *
     * @return
     */
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

    /**
     * 해당 쿠키값을 HTTPS 쿠키값으로 지정함 (secure=true)
     * <p/>
     * 참조: http://en.wikipedia.org/wiki/HTTP_cookie#Secure_and_HttpOnly
     *
     * @return
     */
    public CookiePrototype secure() {
        Cookie cookie = cooker.getCookie(key);
        cookie.setSecure(true);

        return this;
    }

    /**
     * 참조: http://en.wikipedia.org/wiki/HTTP_cookie#Secure_and_HttpOnly
     *
     * @param secure HTTPS 쿠키로 사용할지 여부 지정
     *
     * @return
     */
    public CookiePrototype secure(boolean secure) {
        Cookie cookie = cooker.getCookie(key);
        cookie.setSecure(secure);
        return this;
    }

    /**
     * 참조: http://en.wikipedia.org/wiki/HTTP_cookie#Expires_and_Max-Age
     *
     * @param expirySecond
     * @return
     */
    public CookiePrototype expire(int expirySecond) {
        Cookie cookie = cooker.getCookie(key);
        cookie.setMaxAge(expirySecond);
        return this;
    }

    /**
     * SHA-256 암호화 알고리즘을 사용해 쿠키 값을 암호화 함
     *
     * @return
     */
    public CookiePrototype sha256() {
        return sha256("");
    }

    public CookiePrototype sha256(String saltValue) {
        return sha256(saltValue.getBytes());
    }

    public CookiePrototype sha256(byte[] saltValue) {
        Cookie cookie = cooker.getCookie(key);
        String originalValue = cookie.getValue();
        if (originalValue != null && originalValue.length() > 0) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.reset();
                if (saltValue != null && saltValue.length > 0) {
                    digest.update(saltValue);
                }
                byte[] shaBytes = digest.digest(originalValue.getBytes());

                String shaText = toHexValue(shaBytes);

                if (logger.isDebugEnabled()) {
                    logger.debug("Convert to SHA-256 value: " + originalValue + " to " + shaText);
                }
                cookie.setValue(shaText);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.warn("Cookie 값에 대한 SHA-256 암호화를 수행하려 했으나 값이 비어 있어 수행하지 않았습니다!");
        }
        return this;
    }

    /**
     * 쿠키값의 domain 패턴 설정
     *
     * @param pattern
     */
    public void domain(String pattern) {
        Cookie cookie = cooker.getCookie(key);
        cookie.setDomain(pattern);
    }

    private String toHexValue(byte[] shaBytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < shaBytes.length; i++) {
            String t = Integer.toString((shaBytes[i] & 0xff) + 0x100, 16).substring(1);
            buffer.append(t);
        }
        return buffer.toString();
    }

    public Cookie getCookie() {
        return this.cooker.getCookie(key);
    }

    public Cooker getCooker() {
        return cooker;
    }

    public String getKey() {
        return key;
    }
}
