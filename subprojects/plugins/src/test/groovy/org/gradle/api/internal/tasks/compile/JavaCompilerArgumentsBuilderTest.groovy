/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.api.internal.tasks.compile

import spock.lang.Specification
import org.gradle.api.JavaVersion
import org.gradle.api.internal.file.collections.SimpleFileCollection

class JavaCompilerArgumentsBuilderTest extends Specification {
    def spec = new DefaultJavaCompileSpec()
    def builder = new JavaCompilerArgumentsBuilder(spec)

    def "generates no options for an unconfigured spec"() {
        expect:
        builder.build() == []
    }

    def "generates no -source option when current Jvm Version is used"() {
        spec.sourceCompatibility = JavaVersion.current().toString();

        expect:
        builder.build() == []
    }

    def "generates -source option when compatibility differs from current Jvm version"() {
        spec.sourceCompatibility = "1.4"

        expect:
        builder.build() == ["-source", "1.4"]
    }

    def "generates no -target option when current Jvm Version is used"() {
        spec.targetCompatibility = JavaVersion.current().toString();

        expect:
        builder.build() == []
    }

    def "generates -target option when compatibility differs current Jvm version"() {
        spec.targetCompatibility = "1.4"

        expect:
        builder.build() == ["-target", "1.4"]
    }

    def "generates -d option"() {
        def file = new File("/project/build")
        spec.destinationDir = file

        expect:
        builder.build() == ["-d", file.path]
    }

    def "generates -verbose option"() {
        when:
        spec.compileOptions.verbose = true

        then:
        builder.build() == ["-verbose"]

        when:
        spec.compileOptions.verbose = false

        then:
        builder.build() == []
    }

    def "generates -deprecation option"() {
        when:
        spec.compileOptions.deprecation = true

        then:
        builder.build() == ["-deprecation"]

        when:
        spec.compileOptions.deprecation = false

        then:
        builder.build() == []
    }

    def "generates -nowarn option"() {
        when:
        spec.compileOptions.warnings = true

        then:
        builder.build() == []

        when:
        spec.compileOptions.warnings = false

        then:
        builder.build() == ["-nowarn"]
    }
    
    def "generates -g option"() {
        spec.compileOptions.debugOptions.debugLevel = "source,vars"
        
        when:
        spec.compileOptions.debug = true
        
        then:
        builder.build() == ["-g:source,vars"]
        
        when:
        spec.compileOptions.debug = false

        then:
        builder.build() == ["-g:none"]
    }

    def "generates -encoding option"() {
        spec.compileOptions.encoding = "some-encoding"

        expect:
        builder.build() == ["-encoding", "some-encoding"]
    }

    def "generates -bootclasspath option"() {
        spec.compileOptions.bootClasspath = "/lib/lib1.jar:/lib/lib2.jar"

        expect:
        builder.build() == ["-bootclasspath", "/lib/lib1.jar:/lib/lib2.jar"]
    }

    def "generates -extdirs option"() {
        spec.compileOptions.extensionDirs = "/dir1:/dir2"

        expect:
        builder.build() == ["-extdirs", "/dir1:/dir2"]
    }

    def "generates -classpath option"() {
        def file1 = new File("/lib/lib1.jar")
        def file2 = new File("/lib/lib2.jar")
        spec.classpath = [file1, file2]

        expect:
        builder.build() == ["-classpath", "$file1$File.pathSeparator$file2"]
    }

    def "adds custom compiler args"() {
        spec.compileOptions.compilerArgs = ["-a", "value-a", "-b", "value-b"]

        expect:
        builder.build() == ["-a", "value-a", "-b", "value-b"]
    }

    def "can include/exclude main options"() {
        spec.sourceCompatibility = "1.4"

        when:
        builder.includeMainOptions(true)

        then:
        builder.build() == ["-source", "1.4"]

        when:
        builder.includeMainOptions(false)

        then:
        builder.build() == []
    }

    def "includes main options by default"() {
        spec.sourceCompatibility = "1.4"

        expect:
        builder.build() == ["-source", "1.4"]
    }

    def "can include/exclude launcher options"() {
        spec.compileOptions.forkOptions.with {
            memoryInitialSize = "64m"
            memoryMaximumSize = "1g"
        }

        when:
        builder.includeLauncherOptions(true)

        then:
        builder.build() == ["-J-Xms64m", "-J-Xmx1g"]

        when:
        builder.includeLauncherOptions(false)

        then:
        builder.build() == []
    }

    def "does not include launcher options by default"() {
        spec.compileOptions.forkOptions.with {
            memoryInitialSize = "64m"
            memoryMaximumSize = "1g"
        }

        expect:
        builder.build() == []
    }

    def "can include/exclude source files"() {
        def file1 = new File("/src/Person.java")
        def file2 = new File("Computer.java")
        spec.source = new SimpleFileCollection(file1, file2)

        when:
        builder.includeSourceFiles(true)

        then:
        builder.build() == [file1.path, file2.path]

        when:
        builder.includeSourceFiles(false)

        then:
        builder.build() == []
    }

    def "does not include source files by default"() {
        def file1 = new File("/src/Person.java")
        def file2 = new File("Computer.java")
        spec.source = new SimpleFileCollection(file1, file2)

        expect:
        builder.build() == []
    }
}
