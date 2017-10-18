/*
 * Copyright 2017 michael-simons.eu.
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
package ac.simons.spring.boot.singleuser.demo;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Simple demo application for this starter.
 *
 * @author Michael J. Simons, 2017-10-18
 */
@SpringBootApplication
public class ReactiveApplication {

    @RestController
    static class HelloWorldController {
        @RequestMapping("/hello")
        public Mono<String> hello(final Mono<Principal> principal) {
            return principal
                    .map(Principal::getName)
                    .map(n -> "Hello, " + n + "\n");
        }
    }

    public static void main(final String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }
}
