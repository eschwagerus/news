package com.upday.util;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * A utility class offering some methods for arrays.
 */
public class ArrayUtil {

    private ArrayUtil() {
    }

    /**
     * This method will "flatten" an array of integers and nested arrays of integers into an array of integers
     * only. (E.g. [[1,2,[3]],4] -> [1,2,3,4]). Note that elements which are not of type Integer of List are ignored.
     *
     * @param originalArray the array to flatten
     * @param resultArray   the array to which results are added
     */
    public static void flattenArray(@NotNull List<Object> originalArray, @NotNull List<Integer> resultArray) {

        originalArray.forEach(element -> {

            if (element instanceof List) {
                // recursion
                flattenArray((List<Object>) element, resultArray);
            } else if (element instanceof Integer) {
                // add to result list -> end of recursion
                resultArray.add((Integer) element);
            }
        });
    }

}