/*
 * jlib - Open Source Java Library
 *
 *     www.jlib.org
 *
 *
 *     Copyright 2005-2018 Igor Akkerman
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

import lombok.RequiredArgsConstructor;
import org.jlib.iterable.BidiIterable;
import org.jlib.iterable.BidiIterator;

/**
 * Wrapper for an array allowing it to be used as {@link Iterable}.
 *
 * @param <Item>
 *        type of the items held in the array
 *
 * @author Igor Akkerman
 */
@RequiredArgsConstructor
public class ArrayIterable<Item>
    implements BidiIterable<Item> {

    /** array to traverse */
    private final Item[] array;

    @Override
    public BidiIterator<Item> iterator() {
        return new ArrayIterator<>(array);
    }
}
