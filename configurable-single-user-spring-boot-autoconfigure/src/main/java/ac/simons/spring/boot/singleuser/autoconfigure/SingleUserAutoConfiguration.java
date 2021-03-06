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

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Provides the auto-configuration of one single, user. This configuration is
 * dependent on having Spring Security Core on the classpath (represented by the
 * AuthenticatenManager) and also Spring Security Configuration (represented by
 * the GlobalAuthenticationConfigurerAdapter).
 *
 * @author Michael J. Simons, 2017-10-14
 */
@Configuration
@ConditionalOnClass(UserDetails.class)
@Import({UserDetailsServiceConfiguration.class, ReactiveUserDetailsServiceConfiguration.class})
@EnableConfigurationProperties(SingleUserProperties.class)
public class SingleUserAutoConfiguration {
}
