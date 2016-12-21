/*
 * jlib - Open Source Java Library
 *
 *     www.jlib.org
 *
 *
 *     Copyright 2005-2015 Igor Akkerman
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.jlib.array;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.function.UnaryOperator.identity;
import lombok.experimental.UtilityClass;
import org.jlib.iterator.BidiIterable;
import org.jlib.iterator.BidiIterator;

/**
 * Utility for arrays.
 *
 * @author Igor Akkerman
 */
@UtilityClass
public final class ArrayUtility {

    public static final Object[] NO_OBJECTS = new Object[0];
    public static final String[] NO_STRINGS = new String[0];

    /**
     * Crates an array of Items in a typesafe manner.
     *
     * @param <Item>
     *        type of the items held in the array
     *
     * @param length
     *        integer specifying the array length
     *
     * @return newly created array
     *
     * @throws NegativeArraySizeException
     *         if {@code length < 0}
     */
    @SuppressWarnings("unchecked")
    public static <Item> Item[] array(final int length)
        throws NegativeArraySizeException {
        return (Item[]) new Object[length];
    }

    /**
     * Returns the array implicitely created of the specified {@link Item}s in a typesafe manner.
     *
     * @param <Item>
     *        type of the items held in the array
     *
     * @param items
     *        comma separated sequence of {@link Item}s
     *
     * @return the specified array {@code items} itself
     */
    @SafeVarargs
    public static <Item> Item[] asArray(final Item... items) {
        return items;
    }

    /**
     * Returns a new {@link BidiIterable} adapter for the specified Items.
     *
     * @param <Item>
     *        type of the items held in the array
     *
     * @param items
     *        comma separated sequence of Items to traverse
     *
     * @return {@link BidiIterable} adapter for {@code items}
     */
    @SafeVarargs
    public static <Item> BidiIterable<Item> iterable(final Item... items) {
        return new ArrayIterable<>(items);
    }

    /**
     * Returns a new {@link BidiIterator} over the specified Items.
     *
     * @param <Item>
     *        type of the items held in the array
     *
     * @param items
     *        comma separated sequence of Items to traverse
     *
     * @return {@link BidiIterable} adapter for {@code items}
     */
    @SafeVarargs
    public static <Item> BidiIterator<Item> iterator(final Item... items) {
        return new ArrayIterator<>(items);
    }

    /**
     * Returns the total number of non array items held in the specified array,
     * recursively descending in every array item.
     *
     * @param items
     *        comma separated sequence of {@link Object} items
     *
     * @return integer specifying the total number of itemsnew
     */
    public static int getFlattenedItemsCount(final Object... items) {

        return stream(items)
            .map(item -> item.getClass().isArray() ? getFlattenedItemsCount((Object[]) item) : 1)
            .reduce(0, Integer::sum);
    }

    /**
     * Recursively appends all Items specified as a comma separated list to the
     * specified {@link Collection}.
     *
     * @param items
     *        comma separated sequence of items
     */
    @SafeVarargs
    public static <Item> void flatten(final Collection<? super Item> target, final Item... items) {
        flattenAsStream(items).forEachOrdered(target::add);
    }

    @SuppressWarnings("unchecked")
    public static <Item> Stream<Item> flattenAsStream(final Item[] items) {
        return stream(items)
            .map(item -> item.getClass().isArray() ? flattenAsStream((Item[]) item) : Stream.of(item))
            .flatMap(identity());
    }

    /**
     * Returns an array of all Items specified as a comma separated list to the
     * specified {@link List}, recursively collected from contained arrays.
     *
     * @param <Item>
     *        type of the specified items
     *
     * @param items
     *        comma separated liet of items
     *
     * @return array of all collected Items
     */
    @SuppressWarnings("unchecked")
    public static <Item> Item[] flatten(final Item... items) {
        return (Item[]) flattenAsStream(items).toArray(Object[]::new);
    }

    /**
     * Compares the specified Objects for mutual equality. Two Objects {@code object1}, {@code object2} are considered
     * equal if {@code object1 == object2 == null} or {@code object1.equals(object2)}.
     *
     * @param objects
     *        comma separated sequence of Objects to compare
     *
     * @return {@code true} if all specified Objects are equal or if the specified sequence of Objects is empty;
     *         {@code false} otherwise
     */
    public static boolean allEqual(final Object... objects) {
        return stream(objects)
            .reduce(true, Objects::equals, Boolean::logicalOr);
    }

    /**
     * Returns whether all of the specified {@link Object} references are {@code null}.
     *
     * @param objects
     *        comma separated sequence of {@link Object}s
     *
     * @return {@code true} if all of the specified {@link Object} references are {@code null};
     *         {@code false} otherwise
     */
    private static boolean allNull(final Object... objects) {
        return stream(objects).noneMatch(Objects::nonNull);
    }

    public static <Value, Result>
    Result[] map(final Value[] values, final Function<Value, Result> mapFunction, final IntFunction<Result[]> generator) {
        return stream(values).map(mapFunction).toArray(generator);
    }
}
