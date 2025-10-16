package org.example.java_training.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
//        RestClient restClient = RestClient.builder(
//                new HttpHost("localhost", 9200)
//        ).build();
//
//        RestClientTransport transport = new RestClientTransport(
//                restClient, new JacksonJsonpMapper()
//        );
//
//        return new ElasticsearchClient(transport);
        Header[] defaultHeaders = new Header[]{
                new BasicHeader("Authorization", "ApiKey aE12dTZwa0JscnVINTlFWFVkd3c6RTZsQ29Dc2hEVkYxMG9fc3BqODVHQQ==")
        };

        RestClient restClient = RestClient.builder(
                HttpHost.create("https://java-training-e96952.es.us-central1.gcp.elastic.cloud:443")
        ).setDefaultHeaders(defaultHeaders).build();

        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
