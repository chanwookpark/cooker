cooker
======

HTTP Cookie Utility

1. Cookie CRUD API

    Cooker c = new Cooker();

    javax.servlet.http.Cookie cookie = c.cooking(key1, value1).getCookie();
    name = cookie.getName();
    value = cookie.getValue();

    cookie.setValue("value update");
    chanwook.cooker.CookiePrototype prototype = c.cooking(cookie);

1. Encoding/Decoding support

    Cooker c = new Cooker();
    c.cooking(key, value).encoding();

    Cookie cookie = c.getCookie(key);
    assertThat("Hello+World%21", is(cookie.getValue()));

    CookiePrototype prototype = c.getPrototype(key);
    cookie = prototype.decoding().getCookie();
    assertThat("Hello World", is(cookie.getValue()));

1. Delimited value

    Cooker c = new Cooker();
    HashMap<String, String> item = new HashMap<String, String>();
    item.put("subkey1", "value1");
    item.put("subkey2", "value2");
    item.put("subkey3", "value3");
    CookiePrototype prototype = c.cooking("subSet", item);

    // result


1. TODO
