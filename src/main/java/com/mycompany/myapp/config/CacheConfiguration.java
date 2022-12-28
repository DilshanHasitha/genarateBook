package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Customer.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".booksPages");
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".priceRelatedOptions");
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".booksRelatedOptions");
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".booksAttributes");
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".booksVariables");
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".avatarAttributes");
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".layerGroups");
            createCache(cm, com.mycompany.myapp.domain.SelectedOption.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SelectedOption.class.getName() + ".selectedOptionDetails");
            createCache(cm, com.mycompany.myapp.domain.SelectedOptionDetails.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SelectedOptionDetails.class.getName() + ".selectedOptions");
            createCache(cm, com.mycompany.myapp.domain.BooksPage.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BooksPage.class.getName() + ".pageDetails");
            createCache(cm, com.mycompany.myapp.domain.BooksPage.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.PageLayers.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PageLayers.class.getName() + ".pageElementDetails");
            createCache(cm, com.mycompany.myapp.domain.PageLayers.class.getName() + ".booksPages");
            createCache(cm, com.mycompany.myapp.domain.PageLayersDetails.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PageLayersDetails.class.getName() + ".pageElements");
            createCache(cm, com.mycompany.myapp.domain.PageSize.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BooksOptionDetails.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BooksAttributes.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BooksAttributes.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.BooksVariables.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BooksVariables.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.PriceRelatedOption.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PriceRelatedOption.class.getName() + ".priceRelatedOptionDetails");
            createCache(cm, com.mycompany.myapp.domain.PriceRelatedOption.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.PriceRelatedOptionDetails.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PriceRelatedOptionDetails.class.getName() + ".priceRelatedOptions");
            createCache(cm, com.mycompany.myapp.domain.BooksRelatedOption.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BooksRelatedOption.class.getName() + ".booksRelatedOptionDetails");
            createCache(cm, com.mycompany.myapp.domain.BooksRelatedOption.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.BooksRelatedOptionDetails.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BooksRelatedOptionDetails.class.getName() + ".booksRelatedOptions");
            createCache(cm, com.mycompany.myapp.domain.OptionType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.LayerGroup.class.getName());
            createCache(cm, com.mycompany.myapp.domain.LayerGroup.class.getName() + ".layers");
            createCache(cm, com.mycompany.myapp.domain.LayerGroup.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.AvatarCharactor.class.getName());
            createCache(cm, com.mycompany.myapp.domain.AvatarCharactor.class.getName() + ".avatarAttributes");
            createCache(cm, com.mycompany.myapp.domain.Options.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Options.class.getName() + ".styles");
            createCache(cm, com.mycompany.myapp.domain.Options.class.getName() + ".avatarAttributes");
            createCache(cm, com.mycompany.myapp.domain.Styles.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Styles.class.getName() + ".options");
            createCache(cm, com.mycompany.myapp.domain.AvatarAttributes.class.getName());
            createCache(cm, com.mycompany.myapp.domain.AvatarAttributes.class.getName() + ".avatarCharactors");
            createCache(cm, com.mycompany.myapp.domain.AvatarAttributes.class.getName() + ".options");
            createCache(cm, com.mycompany.myapp.domain.AvatarAttributes.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.Layers.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Layers.class.getName() + ".layerdetails");
            createCache(cm, com.mycompany.myapp.domain.Layers.class.getName() + ".layerGroups");
            createCache(cm, com.mycompany.myapp.domain.LayerDetails.class.getName());
            createCache(cm, com.mycompany.myapp.domain.LayerDetails.class.getName() + ".layers");
            createCache(cm, com.mycompany.myapp.domain.AvatarAttributes.class.getName() + ".styles");
            createCache(cm, com.mycompany.myapp.domain.Selections.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Books.class.getName() + ".selections");
            createCache(cm, com.mycompany.myapp.domain.Character.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Character.class.getName() + ".avatarCharactors");
            createCache(cm, com.mycompany.myapp.domain.StylesDetails.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Styles.class.getName() + ".stylesDetails");
            createCache(cm, com.mycompany.myapp.domain.ImageStoreType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Images.class.getName());
            createCache(cm, com.mycompany.myapp.domain.FontFamily.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
