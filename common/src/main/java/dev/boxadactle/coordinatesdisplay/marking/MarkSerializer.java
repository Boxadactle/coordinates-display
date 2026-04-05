package dev.boxadactle.coordinatesdisplay.marking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import dev.boxadactle.boxlib.math.geometry.Vec3;

/*
 * Format is:
 *
 * CoordinatesDisplayMark:<serializedjson>
 */
public class MarkSerializer {

    public static String serialize(Vec3<Integer> mark) {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(new MarkPoint(mark));
        return "CoordinatesDisplayMark:" + json;
    }

    public static Vec3<Integer> deserialize(String mark) throws MalformedJsonException {
        if (mark.startsWith("CoordinatesDisplayMark:")) {
            try {
                Gson gson = new GsonBuilder().create();
                String json = mark.substring("CoordinatesDisplayMark:".length());
                MarkPoint point = gson.fromJson(json, MarkPoint.class);
                return point.toVec3();
            } catch (Exception e) {
                throw new MalformedJsonException(e);
            }
        }
        throw new MalformedJsonException("Invalid mark format: " + mark);
    }

}
