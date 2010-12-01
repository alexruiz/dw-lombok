/*
 * Created on Nov 30, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the License); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package com.developerworks.lombok.javac;

import java.util.*;

/**
 * Utility methods related to primitives.
 *
 * @author Alex Ruiz
 */
final class Primitives {

  private static final Map<String, String> PRIMITIVE_WRAPPERS = new HashMap<String, String>();
  static {
    PRIMITIVE_WRAPPERS.put(Boolean.TYPE.getName(), Boolean.class.getName());
    PRIMITIVE_WRAPPERS.put(Character.TYPE.getName(), Character.class.getName());
    PRIMITIVE_WRAPPERS.put(Byte.TYPE.getName(), Byte.class.getName());
    PRIMITIVE_WRAPPERS.put(Short.TYPE.getName(), Short.class.getName());
    PRIMITIVE_WRAPPERS.put(Integer.TYPE.getName(), Integer.class.getName());
    PRIMITIVE_WRAPPERS.put(Long.TYPE.getName(), Long.class.getName());
    PRIMITIVE_WRAPPERS.put(Float.TYPE.getName(), Float.class.getName());
    PRIMITIVE_WRAPPERS.put(Double.TYPE.getName(), Double.class.getName());
  }
  
  static String wrapperClassNameFor(String primitiveClassName) {
    return PRIMITIVE_WRAPPERS.get(primitiveClassName);
  }

  private Primitives() {}
}
