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

import java.lang.annotation.*;

import lombok.AccessLevel;

/**
 * Instructs lombok to generate a "bound" setter for an annotated field.
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
