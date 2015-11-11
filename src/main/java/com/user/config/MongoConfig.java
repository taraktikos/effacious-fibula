package com.user.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import static java.util.Collections.singletonList;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    Environment environment;

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("spring.data.mongodb.database");
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(
                singletonList(
                        new ServerAddress(environment.getProperty("spring.data.mongodb.host"), environment.getProperty("spring.data.mongodb.port", Integer.class))
                ),
                singletonList(
                        MongoCredential.createCredential(
                                environment.getProperty("spring.data.mongodb.username"),
                                environment.getProperty("spring.data.mongodb.database"),
                                environment.getProperty("spring.data.mongodb.password").toCharArray()
                        )
                )
        );
    }

}
