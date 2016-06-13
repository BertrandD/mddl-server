package com.commons.SpringConfig;

import com.mongodb.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * @author LEBOC Philippe
 */
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Bean
    public Mongo mongo() throws Exception {
        return new Mongo();
    }

    @Override
    public String getDatabaseName() {
        return "gameserver";
    }

    @Override
    public String getMappingBasePackage() {
        return "com";
    }
}