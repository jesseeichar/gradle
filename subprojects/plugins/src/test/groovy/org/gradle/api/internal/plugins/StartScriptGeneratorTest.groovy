/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.gradle.api.internal.plugins

import spock.lang.Specification
import org.gradle.util.WrapUtil

class StartScriptGeneratorTest extends Specification {

    def generator = new StartScriptGenerator();

    def "classpath for unix script uses slashes as path separator"() {
        given:
            generator.applicationName = "TestApp"
            generator.setClasspath(WrapUtil.toList("path\\to\\Jar.jar"))
            generator.scriptRelPath = "bin"
        when:
            String unixScriptContent = generator.generateUnixScriptContent()
        then:
            unixScriptContent.contains("CLASSPATH=\$APP_HOME/path/to/Jar.jar")
    }

    def "classpath for windows script uses backslash as path separator"() {
        given:
            generator.applicationName = "TestApp"
            generator.setClasspath(WrapUtil.toList("path/to/Jar.jar"))
            generator.scriptRelPath = "bin"
        when:
            String windowsScriptContent = generator.generateWindowsScriptContent()
        then:
            windowsScriptContent.contains("set CLASSPATH=%APP_HOME%\\path\\to\\Jar.jar")
    }
}
