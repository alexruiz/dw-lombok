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
package com.developerworks.lombok;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static lombok.AccessLevel.PUBLIC;

import java.beans.PropertyChangeSupport;
import java.lang.annotation.*;

import lombok.AccessLevel;

/**
 * Instructs lombok to generate a "bound" setter for an annotated field.
 * <p>
 * For example, given this class:
 * 
 * <pre>
 * public class Person {
 * 
 *   &#64;GenerateBoundSetter private String firstName;
 * }
 * </pre>
 * {@code BoundSetterHandler} (for both javac and eclipse) will generate the AST nodes that correspond to this code:
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
 * <strong>Note:</strong> The handler for this annotation assumes that the class declaring the annotated field has a
 * field of type <code>{@link PropertyChangeSupport}</code> with name "propertySupport." You can either add this
 * expected field manually or annotate the class with <code>{@link GenerateJavaBean}</code> to have lombok generate it
 * for you.
 * </p>
 * 
 * @author Alex Ruiz
 */
@Target(FIELD) @Retention(SOURCE)
public @interface GenerateBoundSetter {
  
  /**
   * If you want your setter to be non-public, you can specify an alternate access level here.
   */
  AccessLevel value() default PUBLIC;
}
