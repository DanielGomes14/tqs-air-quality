package tqs.airquality.models;


import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final long timeToLive;
    private int numberRequests;
    private int numberHits;
    private int numberMisses;
    private final Map<String,CacheValue> cacheMap;
    public Cache(){
        this.timeToLive = (60 * 1000);
        this.cacheMap = new HashMap<>();
    }
    public Cache(long timeToLive){
        if(timeToLive < 0) throw new IllegalArgumentException("Time to Live Should be greater than 0 !");
        this.timeToLive=timeToLive;
        this.cacheMap = new HashMap<>();
    }
    public void put(String key, String value){
        this.cacheMap.put(key, new CacheValue(this.timeToLive, value));
    }

    public String get(String key){
        this.numberRequests++;
        String cachedvalue = null;
        if(!hasKey(key)) this.numberMisses++;

        else if(hasExpired(key)) removeCache(key);

        else {
            cachedvalue = this.cacheMap.get(key).getValue();
            this.numberHits++;
        }

        return cachedvalue;
    }

    public boolean hasKey(String key){
        return this.cacheMap.containsKey(key);
    }
    public boolean hasExpired(String key){
        CacheValue c = this.cacheMap.get(key);
        return c.getTimeToLive() < System.currentTimeMillis();
    }
    public void  removeCache(String key){

        if(hasKey(key)) this.cacheMap.remove(key);
    }

    public int getNumberMisses() {
        return numberMisses;
    }

    public int getNumberHits() {
        return numberHits;
    }

    public int getNumberRequests() {
        return numberRequests;
    }
    public long getTimeToLive(){
        return timeToLive;
    }

    public static class CacheValue{
        private final String value;
        private final long timeToLive;
        public CacheValue(long timeToLive, String value){
            this.timeToLive = System.currentTimeMillis() + timeToLive;
            this.value = value;
        }
       public String getValue(){
            return this.value;
       }
       public long getTimeToLive(){
            return this.timeToLive;
       }

    }
}
