/*
 * Created on Nov 30, 2010
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

import static com.developerworks.lombok.javac.Primitives.wrapperClassNameFor;
import static lombok.javac.handlers.JavacHandlerUtil.chainDots;
import lombok.javac.JavacNode;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.*;

/**
 * Simplifies creation of fields.
 * 
 * @author Alex Ruiz
 */
class FieldBuilder {

  static FieldBuilder newField() {
    return new FieldBuilder();
  }

  private Class<?> type;
  private List<JCExpression> typeArgs = List.<JCExpression> nil();
  private Name name;
  private long modifiers;
  private List<JCExpression> args = List.<JCExpression> nil();

  FieldBuilder ofType(Class<?> newType) {
    type = newType;
    return this;
  }

  FieldBuilder ofType(Class<?> newType, JCExpression typeArg, JavacNode node) {
    type = newType;
    JCExpression localTypeArg = typeArg;
    if (localTypeArg instanceof JCTree.JCPrimitiveTypeTree) {
      String wrapper = wrapperClassNameFor(localTypeArg.toString());
      if (wrapper != null) localTypeArg = node.getTreeMaker().Ident(node.toName(wrapper));
    }
    typeArgs = List.of(localTypeArg);
    return this;
  }

  FieldBuilder withName(Name newName) {
    name = newName;
    return this;
  }

  FieldBuilder withModifiers(long newModifiers) {
    modifiers = newModifiers;
    return this;
  }

  FieldBuilder withArgs(JCExpression... newArgs) {
    args = List.from(newArgs);
    return this;
  }

  JCVariableDecl buildWith(JavacNode node) {
    TreeMaker treeMaker = node.getTreeMaker();
    JCExpression classType = chainDots(treeMaker, node, splitNameOf(type));
    JCExpression varType = typeArgs.isEmpty() ? classType : treeMaker.TypeApply(classType, typeArgs);
    JCExpression newVar = treeMaker.NewClass(null, null, varType, args, null);
    return treeMaker.VarDef(treeMaker.Modifiers(modifiers), name, varType, newVar);
  }

  private static String[] splitNameOf(Class<?> type) {
    return type.getName().split("\\.");
  }

  private FieldBuilder() {}
}
