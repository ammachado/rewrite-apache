/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.apache.httpclient4;

import org.junit.jupiter.api.Test;
import org.openrewrite.config.Environment;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

public class CookieConstantsTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec
          .parser(JavaParser.fromJavaVersion().classpath("httpclient", "httpcore"))
          .recipe(Environment.builder()
            .scanRuntimeClasspath("org.openrewrite")
            .build()
            .activateRecipes("org.openrewrite.apache.httpclient4.UpgradeApacheHttpClient_4_5")
          );
    }

    @Test
    void testCookieConstantsMapping() {
        rewriteRun(
          //language=java
          java(
            """
                import org.apache.http.client.params.CookiePolicy;
                
                class A {
                    void method() {
                        String c1 = CookiePolicy.BROWSER_COMPATIBILITY;
                        String c2 = CookiePolicy.NETSCAPE;
                        String c3 = CookiePolicy.RFC_2109;
                        String c4 = CookiePolicy.RFC_2965;
                        String c5 = CookiePolicy.BEST_MATCH;
                        String c6 = CookiePolicy.IGNORE_COOKIES;
                    }
                }
            """, """
                import org.apache.http.client.config.CookieSpecs;
                
                class A {
                    void method() {
                        String c1 = CookieSpecs.BROWSER_COMPATIBILITY;
                        String c2 = CookieSpecs.NETSCAPE;
                        String c3 = CookieSpecs.STANDARD;
                        String c4 = CookieSpecs.STANDARD_STRICT;
                        String c5 = CookieSpecs.BEST_MATCH;
                        String c6 = CookieSpecs.IGNORE_COOKIES;
                    }
                }
            """
          )
        );
    }
}
