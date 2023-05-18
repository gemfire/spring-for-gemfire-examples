/*
 * Copyright 2019 - 2021. VMware, Inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.vmware.gemfire.inline.service;

import java.util.Optional;

import com.vmware.gemfire.inline.repository.ItemRepository;
import org.springframework.data.gemfire.repository.Wrapper;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public String getValue(String key) {
        Optional<String> value = repository.findById(key);
        return value.orElse("NULL");
    }

  public void putValue(String key, String value) {
    repository.save(new Wrapper<>(value, key));
  }
}
