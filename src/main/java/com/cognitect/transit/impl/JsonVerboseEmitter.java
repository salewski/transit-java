// Copyright (c) Cognitect, Inc.
// All rights reserved.

package com.cognitect.transit.impl;

import com.cognitect.transit.Handler;
import com.fasterxml.jackson.core.JsonGenerator;

import java.util.Map;

public class JsonVerboseEmitter extends JsonEmitter {

    public JsonVerboseEmitter(JsonGenerator gen, Map<Class, Handler> handlers) {
        super(gen, handlers);
    }

    @Override
    public void emitString(String prefix, String tag, String s, boolean asMapKey, WriteCache cache) throws Exception {
        String outString = cache.cacheWrite(Util.maybePrefix(prefix, tag, s), asMapKey);
        if(asMapKey)
            gen.writeFieldName(outString);
        else
            gen.writeString(outString);
    }

    @Override
    protected void emitMap(Object o, boolean ignored, WriteCache cache) throws Exception {

        Iterable<Map.Entry> i = ((Iterable<Map.Entry>) o);
        long sz = Util.mapSize(i);

        emitMapStart(sz);
        for (Map.Entry e : i) {
            marshal(e.getKey(), true, cache);
            marshal(e.getValue(), false, cache);
        }
        emitMapEnd();
    }
}