package dev.gemfire.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region("Users")
public record User(@Id String name) {

}
