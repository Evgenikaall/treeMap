package org.intership.intern;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentMapTest {

    private StudentMap<String, Integer> map;
    private static final Integer TEMP_VALUE = 1;

    @BeforeEach
    public void setUp() {
        map = new StudentMap<>();
    }

    @Test
    public void shouldCheckForEmptiness(){
        assertTrue(map.isEmpty());
    }


    @Test
    public void shouldReturnTrueIfMapIsEmpty(){
        fillMapForComparable();
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void shouldCompareMapsOrderedThroughComparator(){
        Map<Student, Integer> actual = new StudentMap<>(new StudentComparator());
        Map<Student, Integer> expected = studentIntegerMap();
        actual.putAll(expected);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void shouldCompareMapsOrderedThroughCommonComparableOnCreatingUseConstructorWithMapParameter(){
        fillMapForComparable();
        Map<String, Integer> actual = new StudentMap<>(map);
        assertEquals(map.entrySet(), actual.entrySet());
    }

    @Test
    public void shouldIncreaseSizeAfterPut() {
        map.put("1", 1);
        assertEquals(1, map.size());
    }


    @Test
    public void shouldIncreaseSizeAfterPutAndDecreaseSizeAfterRemoveAndCheckPreviousElementForAvailability() {
        map.put("1", 1);
        map.put("3", 1);
        map.remove("3");
        assertTrue(map.containsKey("1"));
        assertEquals(1, map.size());
    }

    @Test
    public void shouldCompareDefaultTreeSetWithCustomTreeMapKeySet() {
        fillMapForComparable();
        final Set<String> expected = new TreeSet<>(Arrays.asList("6", "1", "0", "3", "4", "8", "7", "9"));
        final Set<String> actual = map.keySet();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("sourceForRemovingTest")
    void shouldRemoveNodes(Set<String> expected, String forDelete) {
        fillMapForComparable();
        map.remove(forDelete);
        assertEquals(expected, map.keySet());
    }

    @ParameterizedTest
    @MethodSource("sourceForPuttingAndGettingTest")
    void shouldGetValuesFromTreeMapByKeys(String key, Integer expected){
        fillMapForComparable();
        assertEquals(map.get(key), expected);
    }

    @ParameterizedTest
    @MethodSource("sourceForNullPointerExceptionPutTest")
    void shouldThrowIllegalArgumentExceptionOnPut(String key, Integer value){
        assertThrows(IllegalArgumentException.class, () -> map.put(key, value));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnRemove(){
        assertThrows(IllegalArgumentException.class, () -> map.remove(null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOn(){
        assertThrows(IllegalArgumentException.class, () -> map.get(null));
    }


    @ParameterizedTest
    @MethodSource("sourceForPuttingAndGettingTest")
    void shouldPutNodesWithConstValueAndDifferentKeysInMap(String key, Integer expected){
        fillMapForComparable();
        final Integer actual = map.put(key, TEMP_VALUE);
        assertEquals(actual, expected);
    }

    @Test
    void shouldPutAllMapIntoStudentMap(){
        StudentMap<String, Integer> actual = new StudentMap<>();
        Map<String, Integer> mapForPut = new StudentMap<>();
        actual.put("6", 6);
        actual.put("1", 1);
        actual.put("0", 0);
        actual.put("3", 3);
        actual.put("4", 4);
        actual.put("8", 8);
        actual.put("7", 7);

        mapForPut.put("9", 9);

        actual.putAll(mapForPut);
        fillMapForComparable();
        assertEquals(map.entrySet(),actual.entrySet());
        assertEquals(map.values(), actual.values());
    }

    // optional functions

    private void fillMapForComparable() {
        map.put("6", 6);
        // left part
        map.put("1", 1);
        map.put("0", 0);
        map.put("3", 3);
        map.put("4", 4);
        // right part
        map.put("8", 8);
        map.put("7", 7);
        map.put("9", 9);
    }

    private static Stream<Arguments> sourceForRemovingTest() {
        return Stream.of(
                Arguments.of(new TreeSet<>(Arrays.asList("6", "1", "0", "3", "4", "8", "9")), "7"),
                Arguments.of(new TreeSet<>(Arrays.asList("6", "1", "0", "3", "4", "7", "8")), "9"),
                Arguments.of(new TreeSet<>(Arrays.asList("6", "1", "0", "4", "7", "8", "9")), "3"),
                Arguments.of(new TreeSet<>(Arrays.asList("6", "0", "3", "4", "7", "8", "9")), "1"),
                Arguments.of(new TreeSet<>(Arrays.asList("6", "1", "3", "4", "7", "8", "9")), "0"),
                Arguments.of(Collections.emptySet(), "6")
        );
    }

    private static Stream<Arguments> sourceForPuttingAndGettingTest(){
        return Stream.of(
                Arguments.of("6", 6),
                Arguments.of("12", null),
                Arguments.of("9", 9),
                Arguments.of("3", 3),
                Arguments.of("8", 8)
        );
    }

    private static Stream<Arguments> sourceForNullPointerExceptionPutTest(){
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("123", null),
                Arguments.of(null, null)
        );
    }

    private Map<Student, Integer> studentIntegerMap(){
        Student b = new Student("b", LocalDate.of(2020,3,2), "2");
        Student a = new Student("a", LocalDate.of(2020, 3, 2), "3");
        Student d = new Student("d", LocalDate.of(2020, 3, 2), "4");
        Student g = new Student("g", LocalDate.of(2020, 3, 2), "5");
        Student i = new Student("i", LocalDate.of(2020, 3, 2), "55");
        Map<Student, Integer> studentIntegerMap = new TreeMap<>(new StudentComparator());
        studentIntegerMap.put(b, 2);
        studentIntegerMap.put(a, 3);
        studentIntegerMap.put(d, 4);
        studentIntegerMap.put(g, 5);
        studentIntegerMap.put(i, 55);
        return studentIntegerMap;
    }

}