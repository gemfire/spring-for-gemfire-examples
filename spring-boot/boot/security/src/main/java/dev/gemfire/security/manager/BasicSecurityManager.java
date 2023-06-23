// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.security.manager;

import org.apache.geode.security.AuthenticationFailedException;
import org.apache.geode.security.ResourcePermission;
import org.apache.geode.security.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class BasicSecurityManager implements SecurityManager {

    private final static String USER_NAME_HARDCODED = "jdoe";
    private final static String PASSWORD_HARDCODED = "p@55w0rd";
    private final Map<String, User> approvedUsersList = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger("example.app.security");

    @Override
    public void init(final Properties securityProperties) {

        User jdoe = new User(USER_NAME_HARDCODED, PASSWORD_HARDCODED,
                List.of(new ResourcePermission(ResourcePermission.Resource.CLUSTER, ResourcePermission.Operation.MANAGE),
                        new ResourcePermission(ResourcePermission.Resource.DATA, ResourcePermission.Operation.MANAGE),
                        new ResourcePermission(ResourcePermission.Resource.DATA, ResourcePermission.Operation.WRITE)));

        this.approvedUsersList.put(USER_NAME_HARDCODED, jdoe);

    }

    @Override
    public Object authenticate(Properties credentials) throws AuthenticationFailedException {

        String username = credentials.getProperty("security-username");
        String password = credentials.getProperty("security-password");

        if (USER_NAME_HARDCODED.equals(username) && PASSWORD_HARDCODED.equals(password) ) {
            return this.approvedUsersList.get(username);
        } else{
            throw new AuthenticationFailedException("Wrong username/password");
        }
    }

    @Override
    public boolean authorize(Object principal, ResourcePermission resourcePermissionRequested) {
        logger.info("User: " + principal);

        if (principal == null) {
            return false;
        }

        User user = (User)principal;

        for (ResourcePermission userPermission : user.getPermissions()) {
            if (userPermission.implies(resourcePermissionRequested)) {
                return true;
            }
        }

        return false;
    }
}