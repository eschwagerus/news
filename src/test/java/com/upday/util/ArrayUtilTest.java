package com.upday.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayUtilTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void flattenExampleArray() {

        // prepare
        // [[1,2,[3]],4] -> [1,2,3,4]
        List<Object> testArray =
                Arrays.asList(
                        Arrays.asList(1, 2,
                                Collections.singletonList(3)),
                        4);
        log.debug(testArray.toString());

        // test
        List<Integer> result = new ArrayList<>();
        ArrayUtil.flattenArray(testArray, result);
        log.debug(result.toString());

        // verify
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }

    @Test
    public void flattenEmptyArray() {

        // prepare

        // test
        List<Integer> result = new ArrayList<>();
        ArrayUtil.flattenArray(new ArrayList<>(), result);

        // verify
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void flattenArrayWithNonIntegers() {

        // prepare
        List<Object> testArray =
                Arrays.asList(
                        Arrays.asList(1, 2,
                                Collections.singletonList(3)),
                        4,
                        "Not an Integer!");

        // test
        List<Integer> result = new ArrayList<>();
        ArrayUtil.flattenArray(testArray, result);

        // verify
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }
}