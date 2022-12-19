package com.mycompany.myapp.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Utility class for Spring Security.
 */
public final class CommonUtils {

    private CommonUtils() {}

    /**
     * Get the right union of the two List.
     *
     * @return the right union list .
     */
    public static <T> List<T> getRightUnion(List<T> leftList, List<T> rightList) {
        List<T> filteredList = new ArrayList<T>();
        if (leftList != null && rightList != null) {
            for (T object : rightList) {
                if (object != null && leftList.contains(object)) {
                    filteredList.add(object);
                }
            }
        }
        return filteredList;
    }

    /**
     * Get the right union of the two List.
     *
     * @return the right union list .
     */
    public static <T> Page<T> getRightUnion(List<T> pageList, Page<T> rightList, Pageable page, long size) {
        List<T> filteredList = getRightUnion(pageList, rightList.getContent());
        return new PageImpl<>(filteredList, page, size);
    }

    public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean nullSafe(Object T) {
        if (T instanceof String) {
            if (T != null && !((String) T).isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String generateCode(String unsafeCode) {
        String safeCode = "";
        if (unsafeCode != null && !unsafeCode.isEmpty()) {
            safeCode = unsafeCode.replaceAll(" ", "-").toUpperCase();
        }
        return safeCode.isEmpty() ? RandomStringUtils.randomAlphanumeric(4) : safeCode;
    }
}
