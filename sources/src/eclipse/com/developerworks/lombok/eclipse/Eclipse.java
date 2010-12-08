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

import static com.developerworks.lombok.eclipse.MemberChecks.isField;
import static lombok.eclipse.Eclipse.*;
import static org.eclipse.jdt.internal.compiler.ast.TypeReference.baseTypeReference;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_void;
import lombok.eclipse.EclipseNode;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.*;

/**
 * @author Alex Ruiz
 */
final class Eclipse {

  static TypeReference qualifiedTypeReference(Class<?> type, ASTNode source) {
    long p = posNom(source);
    return new QualifiedTypeReference(fromQualifiedName(type.getName()), new long[] { p, p, p });
  }

  private static long posNom(ASTNode source) {
    return (long)source.sourceStart << 32 | source.sourceEnd;
  }

  static Expression stringLiteral(String s, ASTNode source) {
    StringLiteral string = new StringLiteral(s.toCharArray(), source.sourceStart, source.sourceEnd, 0);
    setGeneratedBy(string, source);
    return string;
  }

  static MethodDeclaration methodDeclaration(CompilationResult compilationResult, ASTNode source) {
    MethodDeclaration method = new MethodDeclaration(compilationResult);
    copySourceStartAndEnt(source, method);
    setGeneratedBy(method, source);
    return method;
  }

  static TypeReference voidType(ASTNode source) {
    TypeReference type = baseTypeReference(T_void, 0);
    copySourceStartAndEnt(source, type);
    return type;
  }

  private static void copySourceStartAndEnt(ASTNode src, ASTNode dest) {
    dest.sourceStart = src.sourceStart;
    dest.sourceEnd = src.sourceEnd;
  }

  static TypeDeclaration parentOf(EclipseNode node) {
    if (isField(node)) return (TypeDeclaration) node.up().get();
    for (EclipseNode child : node.down())
      if (isField(child)) return parentOf(child);
    return null;
  }

  private Eclipse() {}
}
