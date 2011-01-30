/*
 * Created on Dec 9, 2010
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
package lombok.core.util;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for <code>{@link Arrays#array(Object...)}</code>.
 *
 * @author Alex Ruiz
 */
public class Arrays_array_Test {

  @Test public void should_return_array_with_given_elements() {
    assertThat(Arrays.array("Yoda", "Luke")).isEqualTo(new String[] { "Yoda", "Luke" });
  }
  
  @Test public void should_return_same_array_as_the_one_passed() {
    Object[] array = { "Yoda" };
    assertThat(Arrays.array(array)).isSameAs(array);
  }
}
