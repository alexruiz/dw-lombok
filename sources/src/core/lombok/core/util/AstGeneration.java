/*
 * Created on Dec 1, 2010
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

import static lombok.AccessLevel.NONE;
import lombok.AccessLevel;

/**
 * Utilities related to AST node generation.
 *
 * @author Alex Ruiz
 */
public final class AstGeneration {

  /**
   * Indicates whether code generation should stop based on the given <code>{@link AccessLevel}</code>.
   * @param level the {@code AccessLevel} to evaluate.
   * @return {@code true} if the given {@code AccessLevel} is equal to <code>{@link AccessLevel#NONE}</code>; 
   * {@code false} otherwise.
   */
  public static boolean shouldStopGenerationBasedOn(AccessLevel level) {
    return level == NONE;
  }

  private AstGeneration() {}
}
