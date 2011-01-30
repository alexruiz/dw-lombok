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
package lombok.javac.handlers;

import static lombok.javac.handlers.JavacHandlerUtil.createFieldAccessor;
import static lombok.javac.handlers.JavacHandlerUtil.FieldAccess.ALWAYS_FIELD;
import lombok.javac.JavacNode;

import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 * @author Alex Ruiz
 */
final class Lombok {

  static JCExpression newFieldAccessor(JavacNode fieldNode) {
    return createFieldAccessor(fieldNode.getTreeMaker(), fieldNode, ALWAYS_FIELD);
  }

  private Lombok() {}
}
