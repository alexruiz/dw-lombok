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
import static com.developerworks.lombok.javac.JCNoType.VoidType;
import static com.developerworks.lombok.javac.MemberChecks.*;
import static com.developerworks.lombok.javac.MethodBuilder.newMethod;
import static com.developerworks.lombok.util.AstGeneration.shouldStopGenerationBasedOn;
import static com.developerworks.lombok.util.Names.nameOfConstantHavingPropertyName;
import static com.sun.tools.javac.code.Flags.*;
import static lombok.core.handlers.TransformationsUtil.NON_NULL_PATTERN;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import static lombok.javac.handlers.LombokBridge.createFieldAccessor;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.core.AnnotationValues;
import lombok.javac.*;

import org.mangosdk.spi.ProviderFor;

import com.developerworks.lombok.GenerateBoundSetter;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;

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
@ProviderFor(JavacAnnotationHandler.class) 
public class BoundSetterHandler implements JavacAnnotationHandler<GenerateBoundSetter> {

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
    generateSetter(fields, annotation.getInstance());
    return true;
  }

  private void generateSetter(Collection<JavacNode> fields, GenerateBoundSetter setter) {
    for (JavacNode fieldNode : fields) {
      String propertyNameFieldName = nameOfConstantHavingPropertyName(fieldNode.getName());
      generatePropertyNameConstant(propertyNameFieldName, fieldNode);
      generateSetter(propertyNameFieldName, setter, fieldNode);
    }
  }

  private void generatePropertyNameConstant(String propertyNameFieldName, JavacNode fieldNode) {
    String propertyName = fieldNode.getName();
    if (fieldAlreadyExists(propertyNameFieldName, fieldNode)) return;
    JCExpression propertyNameExpression = fieldNode.getTreeMaker().Literal(propertyName);
    JCVariableDecl fieldDecl = newField().ofType(String.class)
                                         .withName(fieldNode.toName(propertyNameFieldName))
                                         .withModifiers(PUBLIC | STATIC | FINAL)
                                         .withArgs(propertyNameExpression)
                                         .buildWith(fieldNode);
    injectField(fieldNode, fieldDecl);
  }

  private void generateSetter(String propertyNameFieldName, GenerateBoundSetter setter, JavacNode fieldNode) {
    AccessLevel accessLevel = setter.value();
    if (shouldStopGenerationBasedOn(accessLevel)) return;
    JCVariableDecl fieldDecl = (JCVariableDecl) fieldNode.get();
    String setterName = toSetterName(fieldDecl);
    if (methodAlreadyExists(setterName, fieldNode)) return;
    injectMethod(fieldNode.up(), createSetterDecl(accessLevel, propertyNameFieldName, setterName, fieldNode));
  }

  private JCMethodDecl createSetterDecl(AccessLevel accessLevel, String propertyNameFieldName, String setterName,
      JavacNode fieldNode) {
    JCVariableDecl fieldDecl = (JCVariableDecl) fieldNode.get();
    long access = toJavacModifier(accessLevel) | (fieldDecl.mods.flags & STATIC);
    TreeMaker treeMaker = fieldNode.getTreeMaker();
    List<JCAnnotation> nonNulls = findAnnotations(fieldNode, NON_NULL_PATTERN);
    return newMethod().withModifiers(treeMaker.Modifiers(access))
                      .withName(fieldNode.toName(setterName))
                      .withReturnType(treeMaker.Type(VoidType()))
                      .withParameters(List.of(parameter(nonNulls, fieldNode)))
                      .withBody(body(propertyNameFieldName, fieldNode))
                      .buildWith(treeMaker);
  }

  private JCVariableDecl parameter(List<JCAnnotation> nonNulls, JavacNode fieldNode) {
    JCVariableDecl fieldDecl = (JCVariableDecl) fieldNode.get();
    TreeMaker treeMaker = fieldNode.getTreeMaker();
    return treeMaker.VarDef(treeMaker.Modifiers(0, nonNulls), fieldDecl.name, fieldDecl.vartype, null);
  }

  private JCBlock body(String propertyNameFieldName, JavacNode fieldNode) {
    return fieldNode.getTreeMaker().Block(0, changePropertyValueStatements(propertyNameFieldName, fieldNode));
  }

  private List<JCStatement> changePropertyValueStatements(String propertyNameFieldName, JavacNode fieldNode) {
    Name oldValueName = fieldNode.toName("old");
    JCStatement[] statements = new JCStatement[3];
    statements[0] = oldValueVariableDecl(oldValueName, fieldNode);
    statements[1] = assignNewValueToFieldDecl(fieldNode);
    statements[2] = fireChangeEventMethodDecl(propertyNameFieldName, oldValueName, fieldNode);
    return List.<JCStatement> from(statements);
  }

  private JCStatement oldValueVariableDecl(Name oldValueName, JavacNode fieldNode) {
    TreeMaker treeMaker = fieldNode.getTreeMaker();
    JCVariableDecl varDecl = (JCVariableDecl) fieldNode.get();
    JCExpression init = createFieldAccessor(fieldNode);
    return treeMaker.VarDef(treeMaker.Modifiers(FINAL), oldValueName, varDecl.vartype, init);
  }
  
  private JCStatement assignNewValueToFieldDecl(JavacNode fieldNode) {
    JCVariableDecl fieldDecl = (JCVariableDecl) fieldNode.get();
    TreeMaker treeMaker = fieldNode.getTreeMaker();
    JCExpression fieldRef = createFieldAccessor(fieldNode);
    JCAssign assign = treeMaker.Assign(fieldRef, treeMaker.Ident(fieldDecl.name));
    return treeMaker.Exec(assign);
  }

  private JCStatement fireChangeEventMethodDecl(String propertyNameFieldName, Name oldValueName, JavacNode fieldNode) {
    TreeMaker treeMaker = fieldNode.getTreeMaker();
    Name propertyReferenceName = fieldNode.toName(propertyNameFieldName);
    JCExpression fn = chainDots(treeMaker, fieldNode, "propertySupport", "firePropertyChange");
    List<JCExpression> args = List.<JCExpression> of(treeMaker.Ident(propertyReferenceName), 
                                                     treeMaker.Ident(oldValueName),
                                                     createFieldAccessor(fieldNode));
    JCMethodInvocation m = treeMaker.Apply(List.<JCExpression> nil(), fn, args);
    return treeMaker.Exec(m);
  }
}
