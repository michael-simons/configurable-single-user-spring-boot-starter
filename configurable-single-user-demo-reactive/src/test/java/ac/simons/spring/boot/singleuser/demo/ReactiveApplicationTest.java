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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
import reactor.test.StepVerifier;

/**
 * @author Michael J. Simons, 2017-10-18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {"singleuser.name = peter", "singleuser.password = parker"})
public class ReactiveApplicationTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void helloShouldBeProtected() {
        client
                .mutate()
                    .filter(basicAuthentication("justus", "jones"))
                .build()
                .get().uri("/hello")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String.class);

        final FluxExchangeResult<String> result = client
                .mutate()
                    .filter(basicAuthentication("peter", "parker"))
                .build()
                .get().uri("/hello")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class);

        StepVerifier.create(result.getResponseBody())
                .consumeNextWith(s -> assertThat(s).isEqualTo("Hello, peter\n"))
                .expectComplete()
                .verify();
    }

}
