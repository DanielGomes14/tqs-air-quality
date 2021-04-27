package tqs.airquality.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import tqs.airquality.models.Cache;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CacheTest {
    private Cache cache;
    static long TIME_TO_LIVE = 4;
    @BeforeEach
    void Setup(){
        // this object is used in every test of this file,
        // with exception of the instantiation test
        cache = new Cache(TIME_TO_LIVE);
    }

    @Test
    void instantiationTest(){
        assertThat(cache.getTimeToLive(), is(TIME_TO_LIVE));
        assertThrows(IllegalArgumentException.class,() ->{ new Cache(-4) ;});
    }

    @Test
    void putAndGetTest() {
          String key = "https://someurl.com";
          String invalid_key = "not_stored_key";
          String value = "{\"value\":\"test\",\"value2\":\"test2\"}";
          cache.put(key,value);
          assertEquals(cache.get(key),value);
          assertThat(cache.get(invalid_key)).isNull();
    }
    @Test
    void containsKeyTest(){
        String key = "https://someurl.com";
        String invalid_key = "not_stored_key";
        String value = "{\"value\":\"test\",\"value2\":\"test2\"}";
        cache.put(key,value);
        assertThat(cache.hasKey(key)).isTrue();
        assertThat(cache.hasKey(invalid_key)).isFalse();
    }

    @Test
    void whenGetExistentKey_thenNumberHitsAndRequestsIncrement(){
        String key = "https://someurl.com";
        String value = "{\"value\":\"test\",\"value2\":\"test2\"}";
        cache.put(key,value);
        assertEquals(value,cache.get(key));;
        assertThat(cache.getNumberHits(),is(1));
        assertThat(cache.getNumberMisses(),is(0));
        assertThat(cache.getNumberRequests(),is(1));
    }
    @Test
    void whenGetInvalidKey_thenNumberMissesAndRequestsIncrement(){
        String key = "https://someurl.com";
        cache.get(key);
        assertThat(cache.getNumberRequests(),is(1));
        assertThat(cache.getNumberMisses(),is(1));
        assertThat(cache.getNumberHits(),is(0));
    }

    @Test
    void whenGetExpired_Key_thenRemoveKey(){
        String key = "\"https://someurl.com\"";
        String value = "some_value";
        cache.put(key, value);
        await().atMost(Duration.ofSeconds(TIME_TO_LIVE));
        // first verify that key has expired
        assertThat(cache.hasExpired(key)).isTrue();
        // then verify that key has been removed when it is requested
        assertThat(cache.get(key)).isNull();
        assertThat(cache.hasKey(key)).isFalse();
    }


}
