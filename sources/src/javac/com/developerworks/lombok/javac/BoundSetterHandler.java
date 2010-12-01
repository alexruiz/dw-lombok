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

import static com.developerworks.lombok.javac.FieldBuilder.newField;
import static com.developerworks.lombok.util.Names.nameOfConstantHavingPropertyName;
import static com.sun.tools.javac.code.Flags.*;
import static lombok.core.AST.Kind.FIELD;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import static lombok.javac.handlers.JavacHandlerUtil.MemberExistsResult.NOT_EXISTS;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.core.AnnotationValues;
import lombok.javac.*;

import org.mangosdk.spi.ProviderFor;

import com.developerworks.lombok.GenerateBoundSetter;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

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
 * <strong>Note:</strong> This annotation handler ass
 * </p>
 * 
 * @author Alex Ruiz
 */
@ProviderFor(JavacAnnotationHandler.class) public class BoundSetterHandler implements
    JavacAnnotationHandler<GenerateBoundSetter> {

  private static final Class<GenerateBoundSetter> TARGET_ANNOTATION_TYPE = GenerateBoundSetter.class;

  /**
   * Called when an annotation is found that is likely to match <code>{@link GenerateBoundSetter}</code>.
   * @param annotation the actual annotation.
   * @param ast the javac AST node representing the annotation.
   * @param astWrapper the lombok AST wrapper around {@code ast}.
   * @return {@code true} if this handler successfully handled {@code GenerateBoundSetter}; {@code false} otherwise.
   */
  @Override 
  public boolean handle(AnnotationValues<GenerateBoundSetter> annotation, JCAnnotation ast, JavacNode astWrapper) {
    Collection<JavacNode> fields = astWrapper.upFromAnnotationToFields();
    markAnnotationAsProcessed(astWrapper, TARGET_ANNOTATION_TYPE);
    deleteImportFromCompilationUnit(astWrapper, AccessLevel.class.getName());
    JavacNode mayBeField = astWrapper.up();
    if (mayBeField == null) return false;
    if (!isField(mayBeField)) {
      astWrapper.addError(String.format("@%s is only supported on fields", TARGET_ANNOTATION_TYPE.getName()));
      return true;
    }
    JavacNode typeNode = findTypeNodeFrom(mayBeField);
    generateSetter(fields, typeNode, annotation.getInstance());
    return true;
  }

  private static boolean isField(JavacNode node) {
    return FIELD.equals(node.getKind());
  }

  private static JavacNode findTypeNodeFrom(JavacNode node) {
    JavacNode n = node;
    while (n != null && !isJCClassDecl(n))
      n = n.up();
    if (!isJCClassDecl(n)) return null;
    return n;
  }

  private static boolean isJCClassDecl(JavacNode node) {
    return node != null && node.get() instanceof JCClassDecl;
  }

  private void generateSetter(Collection<JavacNode> fields, JavacNode typeNode, GenerateBoundSetter setter) {
    for (JavacNode fieldNode : fields)
      generateSetter(fieldNode, typeNode, setter);
  }

  private void generateSetter(JavacNode fieldNode, JavacNode typeNode, GenerateBoundSetter setter) {
    generatePropertyNameConstant(fieldNode, typeNode);
  }

  private void generatePropertyNameConstant(JavacNode fieldNode, JavacNode typeNode) {
    String propertyName = fieldNode.getName();
    String nameOfConstant = nameOfConstantHavingPropertyName(propertyName);
    if (!fieldExists(nameOfConstant, typeNode).equals(NOT_EXISTS)) return;
    JCExpression propertyNameExpression = fieldNode.getTreeMaker().Literal(propertyName);
    JCVariableDecl fieldDecl = newField().ofType(String.class)
                                         .withName(fieldNode.toName(nameOfConstant))
                                         .withModifiers(PUBLIC | STATIC | FINAL)
                                         .withArgs(propertyNameExpression)
                                         .buildWith(typeNode);
    injectField(typeNode, fieldDecl);
  }
}
