package bteam.example.ecoolshop.util.maps;

import java.util.HashMap;
import java.util.Map;

public class ResponseMap {
    public static Map<Object, Object> errorResponse(String error) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("ERROR:", error);

        return hashMap;
    }

    public static Map<Object, Object> OkResponse(String key, String value) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(key, value);

        return hashMap;
    }
}
