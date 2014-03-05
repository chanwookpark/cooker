cooker
======

HTTP Cookie Utility

1. Cookie CRUD API

        Cooker c = new Cooker();

        // New & Get Cookie

        javax.servlet.http.Cookie cookie = c.cooking(key1, value1).getCookie();
        name = cookie.getName();
        value = cookie.getValue();

        // Delete Cookie

        c.delete(key);

1. Encoding/Decoding support

        Cooker c = new Cooker();
        c.cooking(key, value).encoding();

        javax.servlet.http.Cookie cookie = c.getCookie(key);
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
        assertThat("subkey2=value2~subkey1=value1~subkey3=value3", is(prototype.getCookie().getValue()));

1. Cookie option

        Cooker c = new Cooker();
        c.cooking("key1", "value1")
            .secure()
            .expire(expirySecond)
        ;

        javax.servlet.http.Cookie cookie = c.getCookie("key1");
        assertTrue(cookie.getSecure());

1. SHA-256 secure algorithm

        Cooker c = new Cooker();
        String key = "password";
        // default
        c.cooking(key, "12345678").sha256();

        javax.servlet.http.Cookie cookie = c.getCookie(key);
        assertThat("ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f",
                is(cookie.getValue()));

        // with salt value
        c.cooking(key, "12345678").sha256("saltValue");

        cookie = c.getCookie(key);
        assertThat("d71e480d67c2c7ba50b1a8f39555e09de9ffb53b6e3482bedf6634aff5b1b068",
                is(cookie.getValue()));

1. Various cookie collection type

        Cooker c = new Cooker();
        c.cooking("key1", "value1");
        c.cooking("key2", "value2");
        c.cooking("key3", "value3");

        List<Cookie> list = c.toList();
        assertNotNull(list);
        assertThat(3, is(list.size()));

        Map<String, Cookie> map = c.toMap();
        assertNotNull(map);
        assertThat(3, is(map.size()));
        assertThat("value1", is(map.get("key1").getValue()));
        assertThat("value2", is(map.get("key2").getValue()));
        assertThat("value3", is(map.get("key3").getValue()));

1. Spring Integration

        // 서블릿 필터를 통해 Request 쿠키값을 Cooker API로 동기화, 다시 Response 쿠키로 옮겨주는 역할을 함


        // 핸들러 메서드(=컨트롤러 메서드)에서 Cooker API를 DI 받도록 WebArgumentResolver를 확장 지원


1. TODO
