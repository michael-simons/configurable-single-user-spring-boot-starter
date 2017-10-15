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
package ac.simons.spring.boot.singleuser.autoconfigure;

import java.util.Arrays;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michael J. Simons, 2017-10-14
 */
public class UserDetailsServiceConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner();

    @Test
    public void configuresADefaultUser() {
        contextRunner
                .withPropertyValues("singleuser.name=michael")
                .withConfiguration(AutoConfigurations.of(SingleUserAutoConfiguration.class))
                .run(context -> {
                    assertThat(context.getBean(UserDetailsService.class).loadUserByUsername("michael")).isNotNull();
                    assertThat(context).getBean(UserDetailsRepository.class).isNull();
                });
    }

    @Test
    public void configuresNoDefaultUserWhenAuthenticationManagerPresent() {
        contextRunner
                .withUserConfiguration(AuthenticationManagerIsPresent.class)
                .withConfiguration(AutoConfigurations.of(SingleUserAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).getBean("singleUserDetailsMananger").isNull();
                });
    }

    @Test
    public void configuresNoDefaultUserWhenAuthenticationProviderPresent() {
        contextRunner
                .withUserConfiguration(AuthenticationProviderIsPresent.class)
                .withConfiguration(AutoConfigurations.of(SingleUserAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).getBean("singleUserDetailsMananger").isNull();
                });
    }

    @Test
    public void configuresNoDefaultUserWhenUserDetailsServicePresent() {
        contextRunner
                .withUserConfiguration(UserDetailsServiceIsPresent.class)
                .withConfiguration(AutoConfigurations.of(SingleUserAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).getBean("singleUserDetailsMananger").isNull();
                });
    }

    @Configuration
    static class AuthenticationManagerIsPresent {

        @Bean
        public AuthenticationManager authenticationManager() {
            return new ProviderManager(Arrays.asList(new DaoAuthenticationProvider()));
        }
    }

    @Configuration
    static class AuthenticationProviderIsPresent {

        @Bean
        public AuthenticationProvider authenticationProvider() {
            return new TestingAuthenticationProvider();
        }
    }

    @Configuration
    static class UserDetailsServiceIsPresent {

        @Bean
        public UserDetailsService userDetailsService() {
            return new InMemoryUserDetailsManager();
        }
    }
}
