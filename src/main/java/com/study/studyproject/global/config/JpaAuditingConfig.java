package com.study.studyproject.global.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *
 *
 */
@Configuration
@EnableJpaAuditing
@EnableCaching
public class JpaAuditingConfig {
}
