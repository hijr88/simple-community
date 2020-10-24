package me.yh.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @EnableJpaAuditing 설정을 따로 빼야 컨트롤러 테스트에서 에러가 안 난다.
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfiguration {

}