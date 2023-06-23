// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.security.manager;

import org.apache.geode.security.ResourcePermission;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

public class User implements Serializable, Principal {

   List<ResourcePermission> userPermissions;
   String userName;
   String userPassword;

   public User(String userName, String userPassword, List<ResourcePermission> userPermissions) {
      this.userName = userName;
      this.userPassword = userPassword;
      this.userPermissions = userPermissions;
   }

   public String getUserPassword() {
      return userPassword;
   }

   @Override
   public String toString() {
      return userName;
   }

   @Override
   public String getName() {
      return userName;
   }

   public List<ResourcePermission> getPermissions() {
      return this.userPermissions;
   }

   public boolean hasPermission(ResourcePermission resourcePermissionRequested) {
      boolean hasPermission = false;

      for (ResourcePermission userPermission : userPermissions) {
         if (userPermission.implies(resourcePermissionRequested)) {
            hasPermission = true;
            break;
         }
      }
      return hasPermission;
   }
}