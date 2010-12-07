/*
 * Created on Dec 6, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package com.developerworks.lombok.util;

import static java.util.Arrays.copyOf;

/**
 * Utilities for arrays.
 *
 * @author Alex Ruiz
 */
public final class Arrays {

  /**
   * Returns a copy of the given array.
   * @param <T> the type of the given array.
   * @param array the given array.
   * @return a copy of the given array.
   */
  public static <T> T[] copy(T[] array) {
    return copyOf(array, array.length);
  }

  private Arrays() {}
}
