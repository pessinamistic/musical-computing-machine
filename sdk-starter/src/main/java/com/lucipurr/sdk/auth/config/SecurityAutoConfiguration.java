package com.lucipurr.sdk.auth.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@EntityScan("com.lucipurr.sdk.core.model")
@EnableJpaRepositories("com.lucipurr.sdk.core.repository")
@ComponentScan("com.lucipurr.sdk.auth")
public class SecurityAutoConfiguration {}
