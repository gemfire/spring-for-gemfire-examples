/*
 * Copyright 2019 - 2021. VMware, Inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.vmware.gemfire.inline.controller;

import com.vmware.gemfire.inline.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    static class Payload {
        public String value;
    }

    final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseBody
    @GetMapping("/{key}")
    public String getValue(@PathVariable String key) {
        System.out.println("key: "+ key);
        String value = itemService.getValue(key);

        return value;
    }

    @PutMapping("/{key}")
    public void putValue(@PathVariable String key, @RequestBody Payload payload) {
        System.out.println("key: "+ key + " value: "+payload.value);
        itemService.putValue(key, payload.value);
    }
}
