package ru.croc.test.util;

import com.google.common.base.Strings;

/**
 * Created by user on 23.07.2016.
 */
public class StringUtil {

    public static final String EMPTY = "";

    public static boolean isEmpty(String str){
        return Strings.isNullOrEmpty(str);
    }

    public static String asNotNull(String str){
        if (isEmpty(str)){
            str = EMPTY;
        }
        return str;
    }

}
