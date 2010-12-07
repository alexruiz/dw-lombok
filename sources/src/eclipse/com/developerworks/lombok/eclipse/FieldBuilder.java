
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
package com.developerworks.lombok.eclipse;

import static com.developerworks.lombok.eclipse.Eclipse.qualifiedTypeReference;
import static com.developerworks.lombok.util.Arrays.copy;
import static lombok.eclipse.Eclipse.setGeneratedBy;
import lombok.eclipse.EclipseNode;

import org.eclipse.jdt.internal.compiler.ast.*;

/**
 * Utility methods related to generation of fields.
 *
 * @author Alex Ruiz
 */
final class FieldBuilder {

  private static final Expression[] NO_ARGS = new Expression[0];

  static FieldBuilder newField() {
    return new FieldBuilder();
  }

  private Class<?> type;
  private String name;
  private int modifiers;
  private Expression[] args = NO_ARGS;
  
  FieldBuilder ofType(Class<?> newType) {
    type = newType;
    return this;
  }

  FieldBuilder withName(String newName) {
    name = newName;
    return this;
  }
  
  FieldBuilder withModifiers(int newModifiers) {
    modifiers = newModifiers;
    return this;
  }
  
  FieldBuilder withArgs(Expression...newArgs) {
    args = copy(newArgs);
    return this;
  }

  FieldDeclaration buildWith(EclipseNode node) {
    ASTNode source = node.get();
    FieldDeclaration fieldDecl = new FieldDeclaration(name.toCharArray(), source.sourceStart, source.sourceEnd);
    setGeneratedBy(fieldDecl, source);
    fieldDecl.declarationSourceEnd = -1;
    fieldDecl.modifiers = modifiers;
    fieldDecl.type = qualifiedTypeReference(type, source);
    setGeneratedBy(fieldDecl.type, source);
    AllocationExpression init = new AllocationExpression();
    setGeneratedBy(init, source);
    init.type = qualifiedTypeReference(type, source);
    setGeneratedBy(init.type, source);
    init.arguments = args;
    fieldDecl.initialization = init;
    return fieldDecl;
  }

  private FieldBuilder() {}
}
