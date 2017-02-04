package com.middlewar.api.config.spring;

import com.middlewar.api.mongo.listeners.BaseMongoEventListener;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leboc Philippe.
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String hostname;

    // TODO: @Value(Integer) doesnt work -> Fixme !
    @Value("${spring.data.mongodb.port:27017}")
    private String port;

    @Override
    protected String getDatabaseName() {
        return this.hostname;
    }

    @Override
    public final Mongo mongo() throws Exception {
        return new MongoClient(this.hostname+":"+this.port);
    }

    @Bean
    public BaseMongoEventListener baseMongoEventListener() {
        return new BaseMongoEventListener();
    }

    @Bean
    @Override
    public CustomConversions customConversions() {
        final List<Converter<?, ?>> converterList = new ArrayList<>();
        // Custom ConverterReader must be add to this list of Converter
        return new CustomConversions(converterList);
    }
}