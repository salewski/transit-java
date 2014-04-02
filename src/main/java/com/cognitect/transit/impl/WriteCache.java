package com.cognitect.transit.impl;

import com.cognitect.transit.Writer;

import java.util.HashMap;
import java.util.Map;

public class WriteCache {

    public static final int MIN_SIZE_CACHEABLE = 3;
    public static final int MAX_CACHE_ENTRIES = 94;
    public static final int BASE_CHAR_IDX = 33;

    private final Map<String, String> cache;
    private int index;

    public WriteCache() {
        index = 0;
        cache = new HashMap<String, String>();
    }

    public static boolean isCacheable(String s, boolean asMayKey) {

        boolean cacheable = false;
        if(s.length() > MIN_SIZE_CACHEABLE) {
            if(asMayKey)
                cacheable = true;
            else if(s.charAt(0) == '~' && (s.charAt(1) == ':' || s.charAt(1) == '$' || s.charAt(1) == '#')) {
                cacheable = true;
            }
        }

        return cacheable;
    }

    private String indexToCode(int index) {

        return Writer.SUB + (char)(index + BASE_CHAR_IDX);
    }

    public String cacheWrite(String s, boolean asMapKey) {

        if(isCacheable(s, asMapKey)) {
            String val = cache.get(s);
            if(val != null)
                return val;
            else {
                if(index == MAX_CACHE_ENTRIES-1) {
                    index = 0;
                    cache.clear();
                }
                String code = indexToCode(index++);
                cache.put(s, code);
            }
        }
        return s;
    }
}