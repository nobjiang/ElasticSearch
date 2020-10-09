package com.example.ela;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ela
 * @description:
 * @author: zhaol
 * @create: 2020-10-08 13:12
 **/
@Configuration
public class ElaticSearchClientConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client=new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.56.181",9200,"http"))
        );
        return client;
    }

}