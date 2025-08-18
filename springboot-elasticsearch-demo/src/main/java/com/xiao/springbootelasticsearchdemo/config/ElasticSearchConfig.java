package com.xiao.springbootelasticsearchdemo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Value("${es.url}")
    private String esServerUrl;

    @Value("${es.username}")
    private String esUsername;

    @Value("${es.password}")
    private String esPassword;

    @Bean
    public ElasticsearchClient esClient() {

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(esUsername, esPassword)
        );

        RestClient restClient = RestClient
                .builder(HttpHost.create(esServerUrl))
                .setHttpClientConfigCallback(hc -> hc.setDefaultCredentialsProvider(credentialsProvider))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//        new ElasticsearchAsyncClient(transport); // 异步客户端对象
        return new ElasticsearchClient(transport); // 同步客户端对象
    }

}
