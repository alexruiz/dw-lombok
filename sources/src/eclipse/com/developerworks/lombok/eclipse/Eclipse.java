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

import static lombok.eclipse.Eclipse.*;
import static org.eclipse.jdt.internal.compiler.ast.TypeReference.baseTypeReference;
import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.AccFinal;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_void;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.*;

/**
 * @author Alex Ruiz
 */
final class Eclipse {

  static Argument argument(char[] name, TypeReference tr, int modifiers, ASTNode source) {
    Argument argument = new Argument(name, posNom(source), tr, modifiers);
    copySourceStartAndEnt(source, argument);
    setGeneratedBy(argument, source);
    return argument;
  }

  static Assignment assignment(Expression lhs, Expression expression, ASTNode source) {
    Assignment assignment = new Assignment(lhs, expression, (int)posNom(source));
    copySourceStartAndEnt(source, assignment);
    setGeneratedBy(assignment, source);
    return assignment;
  }

  static LocalDeclaration localDeclaration(char[] name, TypeReference type, Expression initializer, ASTNode source) {
    LocalDeclaration decl = new LocalDeclaration(name, source.sourceStart, source.sourceEnd);
    decl.modifiers |= AccFinal;
    setGeneratedBy(decl, source);
    decl.type = copyType(type, source);
    setGeneratedBy(decl.type, source);
    decl.initialization = initializer;
    setGeneratedBy(decl.initialization, source);
    return decl;
  }

  static MessageSend messageSend(ASTNode source) {
    MessageSend messageSend = new MessageSend();
    copySourceStartAndEnt(source, messageSend);
    setGeneratedBy(messageSend, source);
    return messageSend;
  }

  static MethodDeclaration methodDeclaration(CompilationResult compilationResult, ASTNode source) {
    MethodDeclaration method = new MethodDeclaration(compilationResult);
    copySourceStartAndEnt(source, method);
    setGeneratedBy(method, source);
    return method;
  }

  static TypeReference qualifiedTypeReference(Class<?> type, ASTNode source) {
    long p = posNom(source);
    return new QualifiedTypeReference(fromQualifiedName(type.getName()), new long[] { p, p, p });
  }

  static Expression referenceForThis(ASTNode source) {
    return new ThisReference(source.sourceStart(), source.sourceEnd());
  }

  static Expression singleNameReference(String name, ASTNode source) {
    return singleNameReference(name.toCharArray(), source);
  }

  static Expression singleNameReference(char[] name, ASTNode source) {
    long pos = posNom(source);
    SingleNameReference ref = new SingleNameReference(name, pos);
    setGeneratedBy(ref, source);
    return ref;
  }

  private static long posNom(ASTNode source) {
    return (long) source.sourceStart << 32 | source.sourceEnd;
  }

  static Expression stringLiteral(String s, ASTNode source) {
    StringLiteral string = new StringLiteral(s.toCharArray(), source.sourceStart, source.sourceEnd, 0);
    setGeneratedBy(string, source);
    return string;
  }

  static TypeReference voidType(ASTNode source) {
    TypeReference type = baseTypeReference(T_void, 0);
    copySourceStartAndEnt(source, type);
    return type;
  }

  private static void copySourceStartAndEnt(ASTNode from, ASTNode to) {
    to.sourceStart = from.sourceStart;
    to.sourceEnd = from.sourceEnd;
  }

  private Eclipse() {}
}
