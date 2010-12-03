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
package com.developerworks.lombok.javac;

import static com.sun.tools.javac.code.Flags.INTERFACE;
import static lombok.core.AST.Kind.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import static lombok.javac.handlers.JavacHandlerUtil.MemberExistsResult.NOT_EXISTS;
import lombok.javac.JavacNode;
import lombok.javac.handlers.JavacHandlerUtil.MemberExistsResult;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

/**
 * @author Alex Ruiz
 */
final class MemberChecks {

  static boolean isClass(JavacNode node) {
    JCTree javacNode = node.get();
    if (!(javacNode instanceof JCClassDecl)) return false;
    JCClassDecl classDecl = (JCClassDecl) javacNode;
    boolean notAClass = (classDecl.mods.flags & (INTERFACE | Flags.ENUM | Flags.ANNOTATION)) != 0;
    return !notAClass;
  }
  
  static boolean isField(JavacNode node) {
    return FIELD.equals(node.getKind());
  }
  
  static boolean fieldAlreadyExists(String fieldName, JavacNode node) {
    return existsYesOrNo(fieldExists(fieldName, node));
  }
  
  static boolean methodAlreadyExists(String methodName, JavacNode node) {
    return existsYesOrNo(methodExists(methodName, node));
  }

  private static boolean existsYesOrNo(MemberExistsResult result) {
    return !result.equals(NOT_EXISTS);
  }
  
  private MemberChecks() {}
}
