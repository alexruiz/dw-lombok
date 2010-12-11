/*
 * Created on Dec 10, 2010
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
import static org.fest.util.Collections.list;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link Names#nameOfConstantBasedOnProperty(String)}</code>.
 *
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class)
public class Names_nameOfConstantBasedOnProperty_Test {

  @Parameters public static Collection<Object[]> parameters() {
    return list(new Object[][] {
        { "i", "PROP_I" },
        { "helloWorld", "PROP_HELLO_WORLD" },
        { "i9K", "PROP_I9_K" }
    });
  }

  private final String propertyName;
  private final String constantName;

  public Names_nameOfConstantBasedOnProperty_Test(String propertyName, String constantName) {
    this.propertyName = propertyName;
    this.constantName = constantName;
  }

  @Test public void should_create_name_of_constant_based_on_property_name() {
    assertThat(Names.nameOfConstantBasedOnProperty(propertyName)).isEqualTo(constantName);
  }
}
