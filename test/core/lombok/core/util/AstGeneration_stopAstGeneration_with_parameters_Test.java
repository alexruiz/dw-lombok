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

import static lombok.AccessLevel.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.util.Collections.list;

import java.util.Collection;

import lombok.AccessLevel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link AstGeneration#stopAstGeneration(AccessLevel)}</code>.
 *
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class)
public class AstGeneration_stopAstGeneration_with_parameters_Test {

  @Parameters public static Collection<Object[]> parameters() {
    return list(new Object[][] {
        { PUBLIC }, { MODULE }, { PROTECTED }, { PACKAGE }, { PRIVATE }
    });
  }
  
  private final AccessLevel level;

  public AstGeneration_stopAstGeneration_with_parameters_Test(AccessLevel level) {
    this.level = level;
  }
  
  @Test public void should_return_false_if_AccessLevel() {
    assertThat(AstGeneration.stopAstGeneration(level)).isFalse();
  }
}
