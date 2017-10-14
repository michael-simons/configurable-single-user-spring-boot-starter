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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Provides the auto-configuration of one single, user. This configuration is
 * dependent on having Spring Security Core on the classpath (represented by the
 * AuthenticatenManager) and also Spring Security Configuration (represented by
 * the GlobalAuthenticationConfigurerAdapter).
 *
 * @author Michael J. Simons, 2017-10-14
 */
@Configuration
@ConditionalOnClass({AuthenticationManager.class, GlobalAuthenticationConfigurerAdapter.class})
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@EnableConfigurationProperties(SingleUserProperties.class)
public class SingleUserAutoConfiguration {

    private static final Log LOG = LogFactory
            .getLog(SingleUserAutoConfiguration.class);

    private final SingleUserProperties singleUserProperties;

    public SingleUserAutoConfiguration(final SingleUserProperties singleUserProperties) {
        this.singleUserProperties = singleUserProperties;
    }

    @ConditionalOnMissingBean({AuthenticationManager.class, AuthenticationProvider.class, UserDetailsService.class})
    @Bean
    public InMemoryUserDetailsManager singleUserDetailsMananger() throws Exception {
        if (singleUserProperties.isDefaultPassword()) {
            LOG.warn(String.format("%n%nUsing generated password %s for user '%s'!%n", singleUserProperties.getPassword(), singleUserProperties.getName()));
        }
        return new InMemoryUserDetailsManager(
                User.withUsername(singleUserProperties.getName())
                        .password(singleUserProperties.getPassword())
                        .roles(singleUserProperties.getRoles().stream().toArray(String[]::new))
                        .build());
    }
}