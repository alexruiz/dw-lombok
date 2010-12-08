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
package com.developerworks.lombok.util;

import static java.lang.Character.*;

import java.beans.*;

/**
 * Utility methods related to names.
 *
 * @author Alex Ruiz
 */
public final class Names {

  /** Name of the field of type <code>{@link PropertyChangeSupport}</code>. */
  public static final String PROPERTY_SUPPORT_FIELD_NAME = "propertySupport";
  
  /** Name of the method "firePropertyChange" in <code>{@link PropertyChangeSupport}</code>. */
  public static final String FIRE_PROPERTY_CHANGE_METHOD_NAME = "firePropertyChange";
  
  /** Name of the method argument of type <code>{@link PropertyChangeListener}</code>. */
  public static final String LISTENER_ARG_NAME = "listener";

  /*** Name of the variable containing the "old" value of a field before it is changed in a setter. */
  public static final String OLD_VALUE_VARIABLE_NAME = "old";

  /**
   * Splits the name of the class using "\." as the regular expression.. For example, {@code java.lang.String} will be
   * split into { "java", "lang", "String" }.
   * @param type the given class.
   * @return the name of the type split using "\." as the regular expression.
   */
  public static String[] splitNameOf(Class<?> type) {
    return type.getName().split("\\.");
  }

  /**
   * Creates the name of the constant that holds the name of a property. For example, if the name of a property is
   * "firstName," this method will return "PROP_FIRST_NAME."
   * @param propertyName the name of the property.
   * @return the name of the constant that holds the name of a property.
   */
  public static String nameOfConstantHavingPropertyName(String propertyName) {
    char[] chars = propertyName.toCharArray();
    StringBuilder b = new StringBuilder();
    b.append("PROP_");
    int charCount = chars.length;
    for (int i = 0; i < charCount; i++) {
      char c = chars[i];
      if (isUpperCase(c) && i > 0) b.append('_');
      if (isLowerCase(c)) c = toUpperCase(c); 
      b.append(c);
    }
    return b.toString();
  }
  
  private Names() {}
}
