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

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.core.userdetails.UserDetailsRepository;

/**
 * Provides the same, configurable single user for reactive security.
 *
 * @author Michael J. Simons, 2017-10-14
 */
@Configuration
@ConditionalOnClass(ReactiveAuthenticationManager.class)
@ConditionalOnMissingBean({ReactiveAuthenticationManager.class, UserDetailsRepository.class})
@ConditionalOnWebApplication(type = Type.REACTIVE)
@AutoConfigureBefore(ReactiveSecurityAutoConfiguration.class)
public class UserDetailsRepositoryConfiguration {

    @Bean
    public MapUserDetailsRepository singleUserDetailsRepository(
            final SingleUserProperties singleUserProperties
    ) {
        return new MapUserDetailsRepository(singleUserProperties.asUser());
    }
}
