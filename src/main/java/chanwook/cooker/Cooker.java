package chanwook.cooker;import javax.servlet.http.Cookie;import javax.servlet.http.HttpServletRequest;import java.util.Collection;import java.util.Map;/** * Created by chanwook on 2014. 3. 1.. */public class Cooker {    private static final String DEFAULT_CHARSET_NAME = "UTF-8";    private static final String DEFAULT_COOKIE_VALUE_DELIMITER = "~";    private String domain;    private String path;    private int expire = 0;    private String charsetName = DEFAULT_CHARSET_NAME;    private String cookieValueDelimiter = DEFAULT_COOKIE_VALUE_DELIMITER;    private CookieStore cookieStore = new InMemoryCookieStore();    public Cooker() {    }    public Cooker(String charsetName) {        this.charsetName = charsetName;    }    public Cooker(String charsetName, String domain) {        this.charsetName = charsetName;        this.domain = domain;    }    public Cooker(String charsetName, String domain, String path) {        this.charsetName = charsetName;        this.domain = domain;        this.path = path;    }    /**     * Cookie를 새로 굽는다     *     * @param key     * @param value     * @return     */    public CookiePrototype cooking(String key, String value) {        Cookie cookie = new Cookie(key, value);        if (this.getDomain() != null && this.getDomain().length() > 0) {            cookie.setDomain(this.getDomain());        }        if (this.getPath() != null && this.getPath().length() > 0) {            cookie.setPath(this.getPath());        }        cookie.setMaxAge(this.expire);        return cooking(cookie);    }    /**     * Cookie를 새로 굽는다     *     * @param cookie     * @return     */    public CookiePrototype cooking(Cookie cookie) {        cookieStore.add(cookie);        return new CookiePrototype(cookie.getName(), this);    }    /**     * 두 번째 파라미터로 전달되는 Map은 delimited로 구분되는 String 문자열로 변환되어 쿠키의 값으로 전달됨     *     * @param key     * @param valueMap     * @return     */    public CookiePrototype cooking(String key, Map<String, String> valueMap) {        StringBuilder b = new StringBuilder();        for (Map.Entry<String, String> e : valueMap.entrySet()) {            b.append(e.getKey());            b.append("=");            b.append(e.getValue());            b.append(cookieValueDelimiter);        }        String cookieValue = b.toString();        if (cookieValue.endsWith(cookieValueDelimiter)) {            cookieValue = cookieValue.substring(0, cookieValue.length() - 1);        }        return cooking(key, cookieValue);    }    /**     * Cookie 삭제. maxAge를 0으로 처리함     *     * @param key     */    public void delete(String key) {        CookiePrototype cookie = getPrototype(key);        cookie.delete();    }    /**     * key에 해당하는 쿠키가 있는지 여부 확인     *     * @param key     * @return     */    public boolean hasCookie(String key) {        if (getCookie(key) != null) {            return true;        }        return false;    }    /**     * 요청 쿠키 정보(HttpServletRequest.getCookies())를 Cooker의 쿠키맵에 저장     *     * @param request     */    public void initialize(HttpServletRequest request) {        this.cookieStore.initialize(request);    }    /**     * 전체 Cookie 값을 List 타입으로 반환     *     * @return     */    public Collection<Cookie> getAllAsList() {        return cookieStore.getAll();    }    /**     * 전체 Cookie 값을 Map 타입으로 반환     *     * @return     */    public Map<String, Cookie> getAllAsMap() {        return cookieStore.getAllAsMap();    }    public Cookie getCookie(String key) {        return cookieStore.get(key);    }    public CookiePrototype getPrototype(String key) {        return new CookiePrototype(key, this);    }    public String getCharsetName() {        return charsetName;    }    public void setCharsetName(String charsetName) {        this.charsetName = charsetName;    }    public String getDomain() {        return domain;    }    public String getCookieValueDelimiter() {        return cookieValueDelimiter;    }    public CookieStore getCookieStore() {        return cookieStore;    }    public void setDomain(String domain) {        this.domain = domain;    }    public String getPath() {        return path;    }    public void setPath(String path) {        this.path = path;    }    public int getExpire() {        return expire;    }    public void setExpire(int expire) {        this.expire = expire;    }    public void setCookieStore(CookieStore cookieStore) {        this.cookieStore = cookieStore;    }    public void setCookieValueDelimiter(String cookieValueDelimiter) {        this.cookieValueDelimiter = cookieValueDelimiter;    }}