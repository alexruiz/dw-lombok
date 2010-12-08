/*
 * Created on Dec 8, 2010
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

import static com.developerworks.lombok.eclipse.Eclipse.*;
import static com.developerworks.lombok.eclipse.FieldBuilder.newField;
import static com.developerworks.lombok.eclipse.MemberChecks.*;
import static com.developerworks.lombok.eclipse.MethodBuilder.newMethod;
import static com.developerworks.lombok.util.Arrays.array;
import static com.developerworks.lombok.util.ErrorMessages.annotationShouldBeUsedInClass;
import static com.developerworks.lombok.util.Names.*;
import static java.lang.reflect.Modifier.*;
import static lombok.eclipse.handlers.EclipseHandlerUtil.*;

import java.beans.*;

import lombok.core.AnnotationValues;
import lombok.eclipse.*;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.mangosdk.spi.ProviderFor;

import com.developerworks.lombok.GenerateJavaBean;

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
@ProviderFor(EclipseAnnotationHandler.class)
public class JavaBeanHandler implements EclipseAnnotationHandler<GenerateJavaBean> {

  private static final Class<GenerateJavaBean> TARGET_ANNOTATION_TYPE = GenerateJavaBean.class;

  /**
   * Called when an annotation is found that is likely to match <code>{@link GenerateJavaBean}</code>. This is were AST
   * node generation happens.
   * @param annotation the actual annotation.
   * @param ast the Eclipse AST node representing the annotation.
   * @param astWrapper the lombok AST wrapper around {@code ast}.
   * @return {@code true} if this handler successfully handled {@code GenerateJavaBean}; {@code false} otherwise.
   */
  @Override 
  public boolean handle(AnnotationValues<GenerateJavaBean> annotation, Annotation ast, EclipseNode astWrapper) {
    EclipseNode typeNode = astWrapper.up();
    if (typeNode == null) return false;
    if (!isClass(typeNode)) {
      astWrapper.addError(annotationShouldBeUsedInClass(TARGET_ANNOTATION_TYPE));
      return true;
    }
    generatePropertyChangeSupportField(typeNode);
    generateChangeListenerMethods(typeNode);
    return true;
  }

  private void generatePropertyChangeSupportField(EclipseNode typeNode) {
    if (fieldAlreadyExists(PROPERTY_SUPPORT_FIELD_NAME, typeNode)) return;
    FieldDeclaration fieldDecl = newField().ofType(PropertyChangeSupport.class)
                                           .withName(PROPERTY_SUPPORT_FIELD_NAME)
                                           .withModifiers(PRIVATE | FINAL)
                                           .withArgs(referenceForThis(typeNode.get()))
                                           .buildWith(typeNode);
    injectField(typeNode, fieldDecl);
  }

  private void generateChangeListenerMethods(EclipseNode typeNode) {
    for (String methodName : PROPERTY_CHANGE_METHOD_NAMES)
      generateChangeListenerMethod(methodName, typeNode);
  }

  private void generateChangeListenerMethod(String methodName, EclipseNode typeNode) {
    MethodDeclaration methodDecl = newMethod().withModifiers(PUBLIC)
                                              .withName(methodName)
                                              .withReturnType(voidType(typeNode.get()))
                                              .withParameters(parameters(typeNode))
                                              .withBody(body(methodName, typeNode))
                                              .buildWith(typeNode);
    injectMethod(typeNode, methodDecl);
  }

  private Argument[] parameters(EclipseNode typeNode) {
    ASTNode source = typeNode.get();
    TypeReference type = qualifiedTypeReference(PropertyChangeListener.class, source);
    Argument parameter = argument(LISTENER_ARG_NAME.toCharArray(), type, FINAL, source);
    return array(parameter);
  }

  private Statement[] body(String methodName, EclipseNode typeNode) {
    return array(delegateToPropertySupport(methodName, typeNode));
  }
  
  private Statement delegateToPropertySupport(String methodName, EclipseNode typeNode) {
    ASTNode source = typeNode.get();
    MessageSend fn = messageSend(source);
    fn.receiver = singleNameReference(PROPERTY_SUPPORT_FIELD_NAME.toCharArray(), source, 0);
    fn.selector = methodName.toCharArray();
    fn.arguments = array(singleNameReference(LISTENER_ARG_NAME, source));
    return fn;
  }
}
