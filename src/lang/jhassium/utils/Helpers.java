package lang.jhassium.utils;

/**
 * File : Helpers.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:14
 */
public class Helpers {
    public static <T> T as(Object o, Class<T> tClass) {
        return tClass.isInstance(o) ? (T) o : null;
    }
}
