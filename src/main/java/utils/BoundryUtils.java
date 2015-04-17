package utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by root on 16/4/15.
 */
public class BoundryUtils {

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean isEmpty(Object value) throws UnsupportedOperationException {

        if(isNull(value)) {
            return false;
        }

        if(value instanceof String) {
            String l_value = (String)value;
            return l_value.isEmpty();
        }

        if(value instanceof Map) {
            Map l_value = (Map)value;
            return l_value.isEmpty();
        }

        if(value instanceof Collection) {
            Collection l_value = (Collection)value;
            return l_value.isEmpty();
        }

        throw new UnsupportedOperationException(""+value);
    }

    public static boolean isNullOrEmpty(Object value) {
        return isNull(value) || isEmpty(value);
    }


}
