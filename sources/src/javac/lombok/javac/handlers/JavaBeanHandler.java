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
package lombok.javac.handlers;

import static com.sun.tools.javac.code.Flags.*;
import static lombok.core.util.ErrorMessages.annotationShouldBeUsedInClass;
import static lombok.core.util.Names.*;
import static lombok.javac.handlers.FieldBuilder.newField;
import static lombok.javac.handlers.JCNoType.voidType;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import static lombok.javac.handlers.MemberChecks.*;
import static lombok.javac.handlers.MethodBuilder.newMethod;

import java.beans.*;

import lombok.GenerateJavaBean;
import lombok.core.AnnotationValues;
import lombok.javac.*;

import org.mangosdk.spi.ProviderFor;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;

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
      astWrapper.addError(annotationShouldBeUsedInClass(TARGET_ANNOTATION_TYPE));
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
    for (String methodName : PROPERTY_CHANGE_METHOD_NAMES)
      generateChangeListenerMethod(methodName, typeNode);
  }

  private void generateChangeListenerMethod(String methodName, JavacNode typeNode) {
    if (methodAlreadyExists(methodName, typeNode)) return;
    TreeMaker treeMaker = typeNode.getTreeMaker();
    JCMethodDecl methodDecl = newMethod().withModifiers(PUBLIC)
                                         .withName(methodName)
                                         .withReturnType(treeMaker.Type(voidType()))
                                         .withParameters(parameters(typeNode))
                                         .withBody(body(methodName, typeNode))
                                         .buildWith(typeNode);
    injectMethod(typeNode, methodDecl);
  }

  private List<JCVariableDecl> parameters(JavacNode typeNode) {
    TreeMaker treeMaker = typeNode.getTreeMaker();
    JCExpression type = chainDots(treeMaker, typeNode, splitNameOf(PropertyChangeListener.class));
    JCVariableDecl parameter = treeMaker.VarDef(treeMaker.Modifiers(FINAL), listenerArgName(typeNode), type, null);
    return List.of(parameter);
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
