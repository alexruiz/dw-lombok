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

import static com.developerworks.lombok.eclipse.Eclipse.methodDeclaration;
import static com.developerworks.lombok.util.Arrays.*;
import static lombok.eclipse.Eclipse.ECLIPSE_DO_NOT_TOUCH_FLAG;
import lombok.eclipse.EclipseNode;

import org.eclipse.jdt.internal.compiler.ast.*;

/**
 * Simplifies creation of methods.
 * 
 * @author Alex Ruiz
 */
class MethodBuilder {

  static MethodBuilder newMethod() {
    return new MethodBuilder();
  }

  private int modifiers;
  private char[] name;
  private TypeReference returnType;
  private Argument[] parameters;
  private TypeReference[] throwsClauses;
  private Annotation[] annotations;
  private Statement[] body;

  private MethodBuilder() {}

  MethodBuilder withModifiers(int newModifiers) {
    modifiers = newModifiers;
    return this;
  }

  MethodBuilder withName(String newName) {
    name = newName.toCharArray();
    return this;
  }

  MethodBuilder withReturnType(TypeReference newReturnType) {
    returnType = newReturnType;
    return this;
  }

  MethodBuilder withParameters(Argument[] newParameters) {
    parameters = copy(newParameters);
    return this;
  }

  MethodBuilder withThrowsClauses(TypeReference[] newThrowsClauses) {
    throwsClauses = copy(newThrowsClauses);
    return this;
  }

  MethodBuilder withBody(Statement[] newBody) {
    body = copy(newBody);
    return this;
  }

  MethodBuilder withAnnotations(Annotation[] newAnnotations) {
    annotations = copy(newAnnotations);
    return this;
  }

  MethodDeclaration buildWith(EclipseNode node) {
    TypeDeclaration parent = Eclipse.parentOf(node);
    ASTNode source = node.get();
    MethodDeclaration method = methodDeclaration(parent.compilationResult, source);
    method.modifiers = modifiers;
    method.returnType = returnType;
    method.arguments = parameters;
    method.selector = name;
    method.thrownExceptions = throwsClauses;
    method.bits |= ECLIPSE_DO_NOT_TOUCH_FLAG;
    method.bodyStart = method.declarationSourceStart = method.sourceStart;
    method.bodyEnd = method.declarationSourceEnd = method.sourceEnd;
    method.statements = body;
    if (isNotEmpty(annotations)) method.annotations = annotations;
    return method;
  }
}
