/**
 *
 */
package com.didiglobal.sds.client.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 *
 * @author manzhizhen
 * @version $Id: ListUtils.java, v 0.1 2016年2月21日 上午10:46:15 Administrator Exp $
 */
public class ListUtils {

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        if (elements == null) {
            return new ArrayList<E>();
        }

        ArrayList<E> list = new ArrayList<E>(elements.length);
        Collections.addAll(list, elements);

        return list;
    }

    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<T>();
    }
}
