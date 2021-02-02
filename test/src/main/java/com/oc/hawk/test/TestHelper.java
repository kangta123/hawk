package com.oc.hawk.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oc.hawk.test.utils.ReflectionUtils;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestHelper {
    private static final EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
        .stringLengthRange(5, 20)
        .randomizationDepth(3)
        .randomize(Long.TYPE, (Randomizer<Long>) TestHelper::anyLong)
        .randomize(BigDecimal.class, (Randomizer<BigDecimal>) TestHelper::anyBigDecimal)
        .randomize(Integer.TYPE, (Randomizer<Integer>) TestHelper::anyInt)
        .charset(StandardCharsets.UTF_8).scanClasspathForConcreteTypes(true)
        .build();
    private static final Random random = new Random();

    public static LocalDate day(int day) {
        return LocalDate.of(2016, 1, day + 1);
    }

    public static <T> List<T> generateMany(Supplier<T> s) {
        return generateMany(s, TestHelper.anyInt(10));
    }

    public static <T> List<T> generateMany(Supplier<T> s, int number) {
        return Stream.generate(s).limit(number).collect(Collectors.toList());
    }

    public static <T> T newInstance(Class<T> type) {
        return enhancedRandom.nextObject(type);
    }

    public static <T> T newInstance(Class<T> type, Long id) {
        T t = newInstance(type);
        ReflectionUtils.setFieldValue(t, "id", id);
        return t;
    }

    public static <T> T randomIn(T... values) {
        return values[RandomUtils.nextInt(values.length)];
    }

    public static boolean anyBoolean() {
        return RandomUtils.nextBoolean();
    }

    /**
     * 1 to 100 rate of true value
     *
     * @param trueSeed
     * @return
     */
    public static boolean anyBoolean(int trueSeed) {
        return anyInt(100) < trueSeed;
    }

    public static LocalTime anyLocalTime() {
        return LocalTime.of(anyInt(23), anyInt(59));
    }

    public static int anyInt() {
        return RandomUtils.nextInt();
    }

    public static Long anyLong(int max) {
        return anyLong(max);
    }

    public static Long anyLong() {
        return RandomUtils.nextLong();
    }

    public static String anyString() {
        return anyString(10);
    }

    public static String anyString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static double anyDouble() {
        return RandomUtils.nextDouble();
    }

    public static int anyInt(int max) {
        return RandomUtils.nextInt(max) + 1; // avoid random number 0
    }

    public static <T> Set<T> randomChooseSubCollection(Set<T> source) {
        Set<T> subCollection = source.stream().filter((obj) -> RandomUtils.nextBoolean()).collect(Collectors.toSet());
        if (subCollection.size() == 0) {
            subCollection.add(source.iterator().next());
        }

        return subCollection;
    }

    public static Stream<String> generateFuzzyString(String partOfName) {
        return Lists.newArrayList(TestHelper.anyString() + partOfName, partOfName + TestHelper.anyString(),
            TestHelper.anyString() + partOfName + TestHelper.anyString()).stream();
    }

    public static <T> Set<T> randomChooseSubCollection(Set<T> source, int size) {
        return randomChooseSubCollection(source, RandomUtils.nextInt(source.size() - size), size);
    }

    public static <T> Set<T> randomChooseSubCollection(Set<T> source, long from, long size) {
        return source.stream().skip(from).limit(size).collect(Collectors.toSet());
    }

    public static BigDecimal anyBigDecimal() {
        return BigDecimal.valueOf(RandomUtils.nextDouble() * anyInt(1000));
    }

    /**
     * 返回随机ID.
     */
    public static long randomId() {
        return random.nextLong();
    }

    public static String randomChars(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

    /**
     * 返回随机名称, prefix字符串+5位随机数字.
     */
    public static String randomName(String prefix) {
        return prefix + random.nextInt(10000);
    }

    /**
     * 从输入list中随机返回一个对象.
     */
    public static <T> T randomOne(List<T> list) {
        return list.get(anyInt(list.size()) - 1);
    }

    /**
     * 从输入list中随机返回n个对象.
     */
    public static <T> List<T> randomSome(List<T> list, int n) {
        Collections.shuffle(list);
        return list.subList(0, n);
    }

    /**
     * 从输入list中随机返回随机个对象.
     */
    public static <T> List<T> randomSome(List<T> list) {
        int size = random.nextInt(list.size());
        if (size == 0) {
            size = 1;
        }
        return randomSome(list, size);
    }

    public static <T> Map<T, T> map(T... obj) {
        Map<T, T> map = Maps.newHashMap();
        T key = null, value;
        for (int i = 0; i < obj.length; i++) {
            if (i % 2 == 0) {
                key = obj[i];
            }
            if (i % 2 == 1) {
                value = obj[i];
                map.put(key, value);
            }
        }
        return map;
    }
}
