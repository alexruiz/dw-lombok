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

import static lombok.eclipse.handlers.EclipseHandlerUtil.createFieldAccessor;
import static lombok.eclipse.handlers.EclipseHandlerUtil.FieldAccess.ALWAYS_FIELD;
import lombok.eclipse.EclipseNode;

import org.eclipse.jdt.internal.compiler.ast.Expression;

/**
 * @author Alex Ruiz
 */
public final class Lombok {

  static Expression newFieldAccessor(EclipseNode fieldNode) {
    return createFieldAccessor(fieldNode, ALWAYS_FIELD, fieldNode.get());
  }

  private Lombok() {}
}
