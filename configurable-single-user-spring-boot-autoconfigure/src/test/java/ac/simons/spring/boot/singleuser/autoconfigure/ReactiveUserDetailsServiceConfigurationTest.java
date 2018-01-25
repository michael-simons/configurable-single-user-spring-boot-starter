/*
 * Copyright 2017-2018 michael-simons.eu.
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
package ac.simons.spring.boot.singleuser.autoconfigure;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.reactive.config.EnableWebFlux;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;

/**
 * @author Michael J. Simons, 2017-10-14
 */
public class ReactiveUserDetailsServiceConfigurationTest {

    private final ReactiveWebApplicationContextRunner contextRunner
            = new ReactiveWebApplicationContextRunner();

    @Test
    public void configuresADefaultUser() {

        contextRunner
                .withUserConfiguration(TestConfig.class)
                .withPropertyValues("singleuser.name=michael")
                .withConfiguration(AutoConfigurations.of(ReactiveSecurityAutoConfiguration.class, SingleUserAutoConfiguration.class))
                .run(context -> {
                    assertThat(context.getBean(ReactiveUserDetailsService.class).findByUsername("michael").block()).isNotNull();
                    assertThat(context).getBean(UserDetailsService.class).isNull();
                });
    }

    @Test
    public void configuresNoDefaultUserWhenAuthenticationManagerPresent() {
        contextRunner
                .withUserConfiguration(TestConfig.class, AuthenticationManagerIsPresent.class)
                .withConfiguration(AutoConfigurations.of(ReactiveSecurityAutoConfiguration.class, SingleUserAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).getBean("singleUserDetailsRepository").isNull();
                });
    }

    @Test
    public void configuresNoDefaultUserWhenUserDetailsServicePresent() {
        contextRunner
                .withUserConfiguration(TestConfig.class, UserDetailsRepositoryIsPresent.class)
                .withConfiguration(AutoConfigurations.of(ReactiveSecurityAutoConfiguration.class, SingleUserAutoConfiguration.class))
                .run(context -> {
                    assertThat(context.getBean(ReactiveUserDetailsService.class).findByUsername("bob").block()).isNotNull();
                    assertThat(context.getBean(ReactiveUserDetailsService.class).findByUsername("michael").block()).isNull();
                    assertThat(context).getBean(UserDetailsService.class).isNull();
                });
    }

    @Configuration
    static class AuthenticationManagerIsPresent {

        @Bean
        public ReactiveAuthenticationManager authenticationManager() {
            return authentication -> null;
        }
    }

    @Configuration
    static class UserDetailsRepositoryIsPresent {

        @Bean
        public ReactiveUserDetailsService userDetailsService() {
            return new MapReactiveUserDetailsService(User.withUsername("Bob").password("bobby").roles("USER").build());
        }
    }

    @Configuration
    @EnableWebFlux
    static class TestConfig {
    }
}
