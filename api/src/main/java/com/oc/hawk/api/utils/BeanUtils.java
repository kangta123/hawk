package com.oc.hawk.api.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BeanUtils {
    private static final String[] COPY_IGNORED_PROPERTIES = {};

    public static String upperFirstChar(String old) {
        return old.substring(0, 1).toUpperCase() + old.substring(1);
    }

    public static <M, N> void copyFromGetter(M obj, N target) {
        for (Field field : target.getClass().getDeclaredFields()) {
            Object value = getter(obj, field.getName());
            BeanUtils.setProperty(target, field.getName(), value);
        }
    }

    public static <M, N> List<N> copyListFromGetter(List<M> result, Supplier<N> supplier) {
        List<N> resultList = Lists.newArrayList();
        result.forEach(c -> {
            N target = supplier.get();
            copyFromGetter(c, target);
            resultList.add(target);
        });
        return resultList;
    }

    public static Object getter(Object obj, String attr) {
        try {

            Method met = obj.getClass().getMethod("get" + upperFirstChar(attr));
            return (met.invoke(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> batchTransform(final Class<T> clazz, Collection srcList) {
        if (CollectionUtils.isEmpty(srcList)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(srcList.size());
        for (Object srcObject : srcList) {
            result.add(transform(clazz, srcObject));
        }
        return result;
    }

    public static <T> T transform(Class<T> clazz, Object src) {
        if (src == null) {
            return null;
        }
        T instance = newInstance(clazz, src);

        org.springframework.beans.BeanUtils.copyProperties(src, instance, getPropertyNames(src, Objects::isNull));
        return instance;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
           return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T newInstance(Class<T> clazz, Object src) {
        if (src == null) {
            return null;
        }
        return newInstance(clazz);
    }

    public static <T> T transform(Class<T> clazz, Object src, String... ignorefieldNames) {
        T instance = newInstance(clazz, src);
        org.springframework.beans.BeanUtils.copyProperties(src, instance, ignorefieldNames);
        for (String property : ignorefieldNames) {
            setProperty(instance, property, null);
        }
        return instance;
    }

    public static String[] getPropertyNames(Object source, Predicate predicate) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            if (!pd.getName().startsWith("_")) {
                Object srcValue = src.getPropertyValue(pd.getName());

                if (predicate.test(srcValue)) {
                    emptyNames.add(pd.getName());
                }
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 用于将一个列表转换为列表中的对象的某个属性映射到列表中的对象
     * <p>
     * <pre>
     * List<UserDTO> userList = userService.queryUsers();
     * Map<Integer, userDTO> userIdToUser = BeanUtil.mapByKey("userId", userList);
     * </pre>
     *
     * @param key 属性名
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapByKey(String key, Collection list) {
        Map<K, V> map = new HashMap<>(list.size());
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        try {
            Class clazz = list.iterator().next().getClass();
            Field field = deepFindField(clazz, key);
            if (field == null) {
                throw new IllegalArgumentException("Could not find the key");
            }
            field.setAccessible(true);
            for (Object o : list) {
                map.put((K) field.get(o), (V) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 根据列表里面的属性聚合
     * <p>
     * <pre>
     * List<ShopDTO> shopList = shopService.queryShops();
     * Map<Integer, List<ShopDTO>> city2Shops = BeanUtil.aggByKeyToList("cityId", shopList);
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> aggByKeyToList(String key, Collection list) {
        Map<K, List<V>> map = new HashMap<>(list.size());
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        try {
            Class clazz = list.iterator().next().getClass();
            Field field = deepFindField(clazz, key);
            if (field == null) {
                throw new IllegalArgumentException("Could not find the key");
            }
            field.setAccessible(true);
            for (Object o : list) {
                K k = (K) field.get(o);
                map.computeIfAbsent(k, k1 -> new ArrayList<>());
                map.get(k).add((V) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 用于将一个对象的列表转换为列表中对象的属性集合
     * <p>
     * <pre>
     * List<UserDTO> userList = userService.queryUsers();
     * Set<Integer> userIds = BeanUtil.toPropertySet("userId", userList);
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <K> Set<K> toPropertySet(String key, Collection list) {
        Set<K> set = new HashSet<>();
        if (CollectionUtils.isEmpty(list)) {
            return set;
        }
        try {
            Class clazz = list.iterator().next().getClass();
            Field field = deepFindField(clazz, key);
            if (field == null) {
                throw new IllegalArgumentException("Could not find the key");
            }
            field.setAccessible(true);
            for (Object o : list) {
                Object value = field.get(o);
                if (Objects.nonNull(value)) {
                    set.add((K) value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    private static Field deepFindField(Class clazz, String key) {
        Field field = null;
        while (!clazz.getName().equals(Object.class.getName())) {
            try {
                field = clazz.getDeclaredField(key);
                if (field != null) {
                    break;
                }
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }

    /**
     * 获取某个对象的某个属性
     */
    public static Object getProperty(Object obj, String fieldName) {
        try {
            Field field = deepFindField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置某个对象的某个属性
     */
    public static void setProperty(Object obj, String fieldName, Object value) {
        try {
            Field field = deepFindField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, ArrayUtils.addAll(getPropertyNames(source, Objects::isNull), ignoreProperties));
    }

    public static void copyEntityProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getPropertyNames(source, Objects::isNull));
    }


    public static <T> T mapToObj(Object map, Class<T> clz) {
        return JsonUtils.SNAKE_OBJECT_MAPPER.convertValue(map, clz);
    }
}

