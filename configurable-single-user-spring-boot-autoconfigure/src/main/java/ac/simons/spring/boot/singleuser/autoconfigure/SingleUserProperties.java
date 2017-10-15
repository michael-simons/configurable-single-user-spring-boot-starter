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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * Configuration properties for a single user. Basically copied over from the
 * old Spring Security autoconfiguration.
 *
 * @author Michael J. Simons, 2017-10-13
 */
@ConfigurationProperties("singleuser")
public class SingleUserProperties {

    private static final Log LOG = LogFactory
            .getLog(SingleUserProperties.class);

    /**
     * Name of the single user.
     */
    private String name = "user";

    /**
     * Password for the single user.
     */
    private String password = UUID.randomUUID().toString();

    /**
     * Granted roles for the single user. Defaults to a list containing the role
     * <code>USER</code>
     */
    private List<String> roles = new ArrayList<>(
            Collections.singletonList("USER"));

    private boolean defaultPassword = true;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        if (password.startsWith("${") && password.endsWith("}")
                || !StringUtils.hasLength(password)) {
            return;
        }
        this.defaultPassword = false;
        this.password = password;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = new ArrayList<>(roles);
    }

    boolean isDefaultPassword() {
        return this.defaultPassword;
    }

    UserDetails asUser() {
        if (this.isDefaultPassword()) {
            LOG.warn(String.format("%n%nUsing generated password %s for user '%s'!%n", this.getPassword(), this.getName()));
        }

        return User.withUsername(this.getName())
                .password(this.getPassword())
                .roles(this.getRoles().stream().toArray(String[]::new))
                .build();
    }
}
