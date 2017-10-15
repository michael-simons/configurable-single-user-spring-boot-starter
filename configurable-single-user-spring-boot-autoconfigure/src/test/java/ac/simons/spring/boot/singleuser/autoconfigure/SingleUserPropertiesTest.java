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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michael J. Simons, 2017-10-13
 */
public class SingleUserPropertiesTest {
    
    @Test
    public void beanShouldWork() {
        final SingleUserProperties singleUserProperties = new SingleUserProperties();
        assertThat(singleUserProperties.isDefaultPassword()).isTrue();
        
        singleUserProperties.setName("name");
        singleUserProperties.setPassword("test");
        singleUserProperties.setRoles(Arrays.asList("FOO", "BAR"));
        
        assertThat(singleUserProperties.getName()).isEqualTo("name");
        assertThat(singleUserProperties.getPassword()).isEqualTo("test");
        assertThat(singleUserProperties.isDefaultPassword()).isFalse();
        assertThat(singleUserProperties.getRoles()).contains("FOO", "BAR");
        
        singleUserProperties.setPassword("${test}");
        assertThat(singleUserProperties.getPassword()).isEqualTo("test");
        singleUserProperties.setPassword("");
        assertThat(singleUserProperties.getPassword()).isEqualTo("test");
    }
    
    @Test
    public void asUserShoudWork() {
        final SingleUserProperties singleUserProperties = new SingleUserProperties();
        singleUserProperties.setPassword("test");
        singleUserProperties.setRoles(Arrays.asList("FOO", "BAR"));
        
        final UserDetails userDetails = singleUserProperties.asUser();
        assertThat(userDetails.getUsername()).isEqualTo("user");
        assertThat(userDetails.getPassword()).isEqualTo("test");
        assertThat(userDetails.getAuthorities()).contains(
                new SimpleGrantedAuthority("ROLE_FOO"), 
                new SimpleGrantedAuthority("ROLE_BAR")
        );
    }
}
