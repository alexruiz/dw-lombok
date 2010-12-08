/*
 * Created on Dec 7, 2010
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
package lombok.eclipse.handlers;

import static lombok.eclipse.handlers.EclipseHandlerUtil.FieldAccess.ALWAYS_FIELD;
import lombok.eclipse.EclipseNode;

import org.eclipse.jdt.internal.compiler.ast.Expression;

/**
 * The purpose of this class is to access package-level methods from lombok.
 *
 * @author Alex Ruiz
 */
public final class LombokBridge {

  private LombokBridge() {}

  /**
   * Creates an expression that reads the field. Will either be {@code this.field} or {@code this.getField()} depending
   * on whether or not there's a getter.
   * @param node a Eclipse-specific version of a lombok class that represents a field.
   * @return the created expression.
   */
  public static Expression createFieldAccessor(EclipseNode node) {
    return EclipseHandlerUtil.createFieldAccessor(node, ALWAYS_FIELD, node.get());
  }
}
