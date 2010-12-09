/*
 * Created on Dec 8, 2010
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

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for <code>{@link Arrays#isNotEmpty(Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Arrays_isNotEmpty_Test {

  @Test public void should_return_false_if_array_is_null() {
    assertThat(Arrays.isNotEmpty(null)).isFalse();
  }

  @Test public void should_return_false_if_array_is_empty() {
    assertThat(Arrays.isNotEmpty(new Object[0])).isFalse();
  }

  @Test public void should_return_true_if_array_is_not_empty() {
    assertThat(Arrays.isNotEmpty(new Object[] { "Yoda" })).isTrue();
  }
}
