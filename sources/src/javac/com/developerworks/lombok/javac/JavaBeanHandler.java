/*
 * Created on Dec 3, 2010
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

import static com.developerworks.lombok.javac.FieldBuilder.newField;
import static com.developerworks.lombok.javac.JCNoType.VoidType;
import static com.developerworks.lombok.javac.MemberChecks.*;
import static com.developerworks.lombok.javac.MethodBuilder.newMethod;
import static com.developerworks.lombok.util.Names.*;
import static com.sun.tools.javac.code.Flags.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;

import org.mangosdk.spi.ProviderFor;

import com.developerworks.lombok.GenerateJavaBean;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

/**
 * Generates basic support for making a class annotated with <code>{@link GenerateJavaBean}</code> a JavaBean.
 * <p>
 * "Basic" JavaBean support means:
 * <ol>
 * <li>Generates a field of type <code>{@link PropertyChangeSupport}</code> with name "propertySupport"</li>
 * <li>Generates the public method {@code addPropertyChangeListener(PropertyChangeListener)} and
 * {@code removePropertyChangeListener(PropertyChangeListener)}</li>
 * <li></li>
 * </ol>
 * </p>
 * <p>
 * For example, given this class:
 *
 * <pre>
 * &#64;GenerateJavaBean
 * public class Person {
 *
 * }
 * </pre>
 * this annotation handler will generate the AST nodes that correspond to this code:
 *
 * <pre>
 * public class Person {
 *
 *   private PropertyChangeSupport propertySupport = new PropertyChangeSupport(this);

 *   public void addPropertyChangeListener(PropertyChangeListener listener) {
 *     propertySupport.addPropertyChangeListener(listener);
 *   }
 *
 *   public void removePropertyChangeListener(PropertyChangeListener listener) {
 *     propertySupport.removePropertyChangeListener(listener);
 *   }
 * }
 * </pre>
 * </p>
 *
 * @author Alex Ruiz
 */
@ProviderFor(JavacAnnotationHandler.class)
public class JavaBeanHandler implements JavacAnnotationHandler<GenerateJavaBean> {

  private static final Class<GenerateJavaBean> TARGET_ANNOTATION_TYPE = GenerateJavaBean.class;

  /**
   * Called when an annotation is found that is likely to match <code>{@link GenerateJavaBean}</code>. This is were AST
   * node generation happens.
   * @param annotation the actual annotation.
   * @param ast the javac AST node representing the annotation.
   * @param astWrapper the lombok AST wrapper around {@code ast}.
   * @return {@code true} if this handler successfully handled {@code GenerateJavaBean}; {@code false} otherwise.
   */
  @Override
  public boolean handle(AnnotationValues<GenerateJavaBean> annotation, JCAnnotation ast, JavacNode astWrapper) {
    markAnnotationAsProcessed(astWrapper, TARGET_ANNOTATION_TYPE);
    JavacNode typeNode = astWrapper.up();
    if (typeNode == null) return false;
    if (!isClass(typeNode)) {
      astWrapper.addError(String.format("@%s is only supported on classes", TARGET_ANNOTATION_TYPE.getName()));
      return true;
    }
    generatePropertyChangeSupportField(typeNode);
    generateChangeListenerMethods(typeNode);
    return true;
  }

  private void generatePropertyChangeSupportField(JavacNode typeNode) {
    if (fieldAlreadyExists(PROPERTY_SUPPORT_FIELD_NAME, typeNode)) return;
    JCExpression expressionForThis = chainDots(typeNode.getTreeMaker(), typeNode, "this");
    JCVariableDecl fieldDecl = newField().ofType(PropertyChangeSupport.class)
                                         .withName(PROPERTY_SUPPORT_FIELD_NAME)
                                         .withModifiers(PRIVATE | FINAL)
                                         .withArgs(expressionForThis)
                                         .buildWith(typeNode);
    injectField(typeNode, fieldDecl);
  }

  private void generateChangeListenerMethods(JavacNode typeNode) {
    generateChangeListenerMethod("addPropertyChangeListener", typeNode);
    generateChangeListenerMethod("removePropertyChangeListener", typeNode);
  }

  private void generateChangeListenerMethod(String methodName, JavacNode typeNode) {
    TreeMaker treeMaker = typeNode.getTreeMaker();
    JCMethodDecl method = newMethod().withModifiers(PUBLIC)
                                     .withName(methodName)
                                     .withReturnType(treeMaker.Type(VoidType()))
                                     .withParameters(List.of(parameter(typeNode)))
                                     .withBody(body(methodName, typeNode))
                                     .buildWith(typeNode);
    injectMethod(typeNode, method);
  }

  private JCVariableDecl parameter(JavacNode typeNode) {
    TreeMaker treeMaker = typeNode.getTreeMaker();
    JCExpression type = chainDots(treeMaker, typeNode, splitNameOf(PropertyChangeListener.class));
    return treeMaker.VarDef(treeMaker.Modifiers(FINAL), listenerArgName(typeNode), type, null);
  }

  private JCBlock body(String methodName, JavacNode typeNode) {
    TreeMaker treeMaker = typeNode.getTreeMaker();
    JCExpression delegateToPropertySupport = delegateToPropertySupport(methodName, typeNode);
    List<JCStatement> statements = List.<JCStatement> of(treeMaker.Exec(delegateToPropertySupport));
    return treeMaker.Block(0, statements);
  }

  private JCExpression delegateToPropertySupport(String methodName, JavacNode typeNode) {
    TreeMaker treeMaker = typeNode.getTreeMaker();
    JCExpression fn = chainDots(treeMaker, typeNode, PROPERTY_SUPPORT_FIELD_NAME, methodName);
    JCExpression arg = treeMaker.Ident(listenerArgName(typeNode));
    return treeMaker.Apply(List.<JCExpression> nil(), fn, List.of(arg));
  }

  private Name listenerArgName(JavacNode typeNode) {
    return typeNode.toName(LISTENER_ARG_NAME);
  }

  /**
   * Indicates whether this handler requires resolution.
   * @return {@code false}.
   */
  @Override public boolean isResolutionBased() {
    return false;
  }
}
