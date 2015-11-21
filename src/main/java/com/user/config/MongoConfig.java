package com.user.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import static com.mongodb.MongoCredential.createCredential;
import static java.util.Collections.singletonList;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    private MongoProperties properties;

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(singletonList(createServerAddress()), singletonList(createMongoCredential()));
    }

    private ServerAddress createServerAddress() {
        return new ServerAddress(properties.getHost(), properties.getPort());
    }

    private MongoCredential createMongoCredential() {
        return createCredential(properties.getUsername(), properties.getDatabase(), properties.getPassword());
    }

}
