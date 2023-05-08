package dev.gemfire.expiration.entity.config;

import dev.gemfire.expiration.entity.domain.Order;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableExpiration;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

@Configuration
@EnableExpiration
@EnablePdx
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
public class EntityDefinedExpirationClientConfig {
    @Bean("Orders")
    public ClientRegionFactoryBean<Long, Order> createOrderRegion(GemFireCache gemFireCache) {
        ClientRegionFactoryBean<Long, Order> regionFactoryBean = new ClientRegionFactoryBean<>();
        regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setShortcut(ClientRegionShortcut.CACHING_PROXY);
        regionFactoryBean.setName("Orders");
        return regionFactoryBean;
    }
}
