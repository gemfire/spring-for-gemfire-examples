package com.vmware.gemfire.inline.repository;


import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.GemfireRepository;

@Region("/item")
public interface ItemRepository extends GemfireRepository<String, String> {
}
