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
package lombok.eclipse.handlers;

import static lombok.core.AST.Kind.FIELD;
import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.*;

import static lombok.eclipse.handlers.EclipseHandlerUtil.*;
import static lombok.eclipse.handlers.EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil.MemberExistsResult;

import org.eclipse.jdt.internal.compiler.ast.*;

/**
 * @author Alex Ruiz
 */
final class MemberChecks {
  
  static boolean isClass(EclipseNode node) {
    ASTNode astNode = node.get();
    if (!(astNode instanceof TypeDeclaration)) return false;
    TypeDeclaration classDecl = (TypeDeclaration) astNode;
    return (classDecl.modifiers & (AccInterface | AccAnnotation | AccEnum)) == 0;
  }

  static boolean isField(EclipseNode node) {
    return FIELD.equals(node.getKind());
  }

  static boolean fieldAlreadyExists(String fieldName, EclipseNode node) {
    return existsYesOrNo(fieldExists(fieldName, node));
  }

  static boolean methodAlreadyExists(String methodName, EclipseNode node) {
    return existsYesOrNo(methodExists(methodName, node));
  }

  private static boolean existsYesOrNo(MemberExistsResult result) {
    return !result.equals(NOT_EXISTS);
  }

  private MemberChecks() {}
}
