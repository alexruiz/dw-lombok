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

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.*;

/**
 * Instructs lombok to generate the necessary code to make an annotated Java class a JavaBean.
 * <p>
 * For example, given this class:
 * 
 * <pre>
 * &#64;GenerateJavaBean
 * public class Person {
 * 
 * }
 * </pre>
 * our lombok annotation handlers (for both javac and eclipse) will generate the AST nodes that correspond to this code:
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
@Target(TYPE) @Retention(SOURCE) 
public @interface GenerateJavaBean {}
