/*
 * Created on Dec 4, 2010
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

import static lombok.DirectoryRunner.Compiler.DELOMBOK;

import java.io.File;

import lombok.*;
import lombok.DirectoryRunner.Compiler;
import lombok.DirectoryRunner.TestParams;

import org.junit.runner.RunWith;

/**
 * @author Alex Ruiz
 */
@RunWith(DirectoryRunner.class)
public class TestWithDelombok implements TestParams {

  @Override public Compiler getCompiler() {
    return DELOMBOK;
  }

  @Override public boolean printErrors() {
    return true;
  }

  @Override public File getBeforeDirectory() {
    return new File("test/transform/resource/before");
  }

  @Override public File getAfterDirectory() {
    return new File("test/transform/resource/after-delombok");
  }

  @Override public File getMessagesDirectory() {
    return new File("test/transform/resource/messages-delombok");
  }
}
