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
package com.developerworks.lombok;

import static lombok.eclipse.Eclipse.*;

import org.eclipse.jdt.internal.compiler.ast.*;

/**
 * @author Alex Ruiz
 */
final class Eclipse {

  static TypeReference qualifiedTypeReference(Class<?> type, ASTNode source) {
    long p = posNom(source);
    QualifiedTypeReference ref = new QualifiedTypeReference(fromQualifiedName(type.getName()), new long[] { p, p, p });
    setGeneratedBy(ref, source);
    return ref;
  }

  private static long posNom(ASTNode source) {
    return (long)source.sourceStart << 32 | source.sourceEnd;
  }

  static Expression stringLiteral(String s, ASTNode source) {
    StringLiteral lit = new StringLiteral(s.toCharArray(), source.sourceStart, source.sourceEnd, 0);
    setGeneratedBy(lit, source);
    return lit;
  }

  private Eclipse() {}
}
