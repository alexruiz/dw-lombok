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

import static com.developerworks.lombok.Eclipse.stringExpression;
import static com.developerworks.lombok.FieldBuilder.newField;
import static com.developerworks.lombok.MemberChecks.*;
import static com.developerworks.lombok.util.AstGeneration.shouldStopGenerationBasedOn;
import static com.developerworks.lombok.util.ErrorMessages.annotationShouldBeUsedInField;
import static com.developerworks.lombok.util.Names.nameOfConstantHavingPropertyName;
import static java.lang.reflect.Modifier.*;
import static lombok.eclipse.handlers.EclipseHandlerUtil.injectField;

import java.beans.PropertyChangeSupport;
import java.util.*;

import lombok.AccessLevel;
import lombok.core.AnnotationValues;
import lombok.eclipse.*;

import org.eclipse.jdt.internal.compiler.ast.*;

import com.developerworks.lombok.javac.JavaBeanHandler;

/**
/**
 * Generates a "bound" setter for a field annotated with <code>{@link GenerateBoundSetter}</code>.
 * <p>
 * For example, given this class:
 *
 * <pre>
 * public class Person {
 *
 *   &#64;GenerateBoundSetter private String firstName;
 * }
 * </pre>
 * this annotation handler will generate the AST nodes that correspond to this code:
 *
 * <pre>
 * public class Person {
 *
 *   public static final String PROP_FIRST_NAME = "firstName";
 *
 *   private String firstName;
 *
 *   public void setFirstName(String value) {
 *      String oldValue = firstName;
 *      firstName = value;
 *      propertySupport.firePropertyChange(PROP_FIRST_NAME, oldValue, firstName);
 *   }
 * }
 * </pre>
 * </p>
 * <p>
 * <strong>Note:</strong> This annotation handler assumes that the class declaring the annotated field has a field
 * of type <code>{@link PropertyChangeSupport}</code> with name "propertySupport." You can either add this expected
 * field manually or annotate the class with <code>{@link GenerateJavaBean}</code> to have
 * <code>{@link JavaBeanHandler}</code> generate it for you.
 * </p>
 *
 * @author Alex Ruiz
 */
public class BoundSetterHandler implements EclipseAnnotationHandler<GenerateBoundSetter> {

  private static final Class<GenerateBoundSetter> TARGET_ANNOTATION_TYPE = GenerateBoundSetter.class;

  /**
   * Called when an annotation is found that is likely to match <code>{@link GenerateBoundSetter}</code>. This is were
   * AST node generation happens.
   * @param annotation the actual annotation.
   * @param ast the javac AST node representing the annotation.
   * @param astWrapper the lombok AST wrapper around {@code ast}.
   * @return {@code true} if this handler successfully handled {@code GenerateBoundSetter}; {@code false} otherwise.
   */
  @Override 
  public boolean handle(AnnotationValues<GenerateBoundSetter> annotation, Annotation ast, EclipseNode astWrapper) {
    List<EclipseNode> fields = new ArrayList<EclipseNode>(astWrapper.upFromAnnotationToFields());
    EclipseNode mayBeField = astWrapper.up();
    if (mayBeField == null) return false;
    if (!isField(mayBeField)) {
      astWrapper.addError(annotationShouldBeUsedInField(TARGET_ANNOTATION_TYPE));
      return true;
    }
    EclipseNode typeNode = findTypeNodeFrom(mayBeField);
    generateSetter(fields, annotation.getInstance(), typeNode);
    return true;
  }

  private EclipseNode findTypeNodeFrom(EclipseNode node) {
    EclipseNode n = node;
    while (n != null && !isTypeDeclaration(node)) n = n.up();
    if (!isTypeDeclaration(n)) return null;
    return n;
  }

  private boolean isTypeDeclaration(EclipseNode node) {
    return node != null && node.get() instanceof TypeDeclaration;
  }

  private void generateSetter(List<EclipseNode> fields, GenerateBoundSetter setter, EclipseNode typeNode) {
    for (EclipseNode fieldNode : fields) {
      String propertyNameFieldName = nameOfConstantHavingPropertyName(fieldNode.getName());
      generatePropertyNameConstant(propertyNameFieldName, fieldNode, typeNode);
      generateSetter(propertyNameFieldName, setter, fieldNode);
    }
  }

  private void generatePropertyNameConstant(String propertyNameFieldName, EclipseNode fieldNode, EclipseNode typeNode) {
    String propertyName = fieldNode.getName();
    if (fieldAlreadyExists(propertyNameFieldName, fieldNode)) return;
    Expression propertyNameExpression = stringExpression(propertyName, typeNode.get());
    FieldDeclaration fieldDecl = newField().ofType(String.class) 
                                           .withName(propertyNameFieldName)
                                           .withModifiers(PUBLIC | STATIC | FINAL)
                                           .withArgs(propertyNameExpression)
                                           .buildWith(typeNode);
    injectField(typeNode, fieldDecl);
  }

  private void generateSetter(String propertyNameFieldName, GenerateBoundSetter setter, EclipseNode fieldNode) {
    AccessLevel accessLevel = setter.value();
    if (shouldStopGenerationBasedOn(accessLevel)) return;
    
  }
}
