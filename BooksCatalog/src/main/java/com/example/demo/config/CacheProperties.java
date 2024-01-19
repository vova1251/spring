package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties
public class CacheProperties {

    private final List<String> cacheNames = new ArrayList<>();
    private final Map<String, CacheProperties> caches = new HashMap<>();

    public interface CacheNames {
        String NANE_AND_AUTHOR = "nameAndAuthor";
        String CATEGORY = "category";

    }

    public enum CacheType {
        REDIS
    }

}
