package com.xiao.springbootelasticsearchdemo.controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.xiao.springbootelasticsearchdemo.utils.ESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 索引操作
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private ESUtil esUtil;

    @Autowired
    private ElasticsearchClient esClient;


    @RequestMapping("/operation")
    public void operation() throws IOException {
        String indexName = "user";

        // 判断索引是否存在
        BooleanResponse exists = esClient.indices().exists(b -> b.index(indexName));
        if (exists.value()) {
            System.out.println("索引已存在");
            return;
        }

        // 创建索引
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(indexName).build();
        CreateIndexResponse createIndexResponse = esClient.indices().create(createIndexRequest);
        System.out.println("创建索引的响应对象:" + createIndexResponse);

        // 查询索引
        GetIndexRequest getIndexRequest = new GetIndexRequest.Builder().index(indexName).build();
        GetIndexResponse getIndexResponse = esClient.indices().get(getIndexRequest);
        System.out.println("查询索引的响应对象:" + getIndexResponse);

        // 修改索引，补充字段
        PutMappingRequest putMappingRequest = new PutMappingRequest.Builder().index(indexName).properties("phone", p -> p.integer(i -> i)).build();
        PutMappingResponse putMappingResponse = esClient.indices().putMapping(putMappingRequest);
        System.out.println("更新映射的响应对象:" + putMappingResponse);

        // 查询索引
        GetIndexRequest getIndexRequest2 = new GetIndexRequest.Builder().index(indexName).build();
        GetIndexResponse getIndexResponse2 = esClient.indices().get(getIndexRequest2);
        System.out.println("查询索引的响应对象:" + getIndexResponse2);

        // 删除索引
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(indexName).build();
        DeleteIndexResponse deleteIndexResponse = esClient.indices().delete(deleteIndexRequest);
        System.out.println("删除索引的响应对象:" + deleteIndexResponse);

        // 查询索引是否存在
        BooleanResponse exists2 = esClient.indices().exists(b -> b.index(indexName));
        if (!exists2.value()) {
            System.out.println("索引不存在");
        }
    }

    /*
     # HTTP/1.1 404 Not Found
     # X-elastic-product: Elasticsearch
     # content-type: application/vnd.elasticsearch+json;compatible-with=8
     # content-length: 353
     #
     2025-08-12T17:11:53.238+08:00 TRACE 13836 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX PUT 'http://127.0.0.1:9200/user' -d '{}'
     # HTTP/1.1 200 OK
     # X-elastic-product: Elasticsearch
     # content-type: application/vnd.elasticsearch+json;compatible-with=8
     # content-length: 63
     #
     # {"acknowledged":true,"shards_acknowledged":true,"index":"user"}
     创建索引的响应对象:CreateIndexResponse: {"index":"user","shards_acknowledged":true,"acknowledged":true}
     2025-08-12T17:11:53.266+08:00 TRACE 13836 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX GET 'http://127.0.0.1:9200/user'
     # HTTP/1.1 200 OK
     # X-elastic-product: Elasticsearch
     # content-type: application/vnd.elasticsearch+json;compatible-with=8
     # Transfer-Encoding: chunked
     #
     # {"user":{"aliases":{},"mappings":{},"settings":{"index":{"routing":{"allocation":{"include":{"_tier_preference":"data_content"}}},"number_of_shards":"1","provided_name":"user","creation_date":"1754989913010","number_of_replicas":"1","uuid":"9FzPsBRYQ4m2iYvn-2owRQ","version":{"created":"8100099"}}}}}
     查询索引的响应对象:GetIndexResponse: {"user":{"aliases":{},"mappings":{},"settings":{"index":{"number_of_shards":"1","number_of_replicas":"1","routing":{"allocation":{"include":{"_tier_preference":"data_content"}}},"provided_name":"user","creation_date":1754989913010,"uuid":"9FzPsBRYQ4m2iYvn-2owRQ","version":{"created":"8100099"}}}}}
     2025-08-12T17:11:53.359+08:00 TRACE 13836 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX PUT 'http://127.0.0.1:9200/user/_mapping' -d '{"properties":{"phone":{"type":"integer"}}}'
     # HTTP/1.1 200 OK
     # X-elastic-product: Elasticsearch
     # content-type: application/vnd.elasticsearch+json;compatible-with=8
     # content-length: 21
     #
     # {"acknowledged":true}
     更新映射的响应对象:PutMappingResponse: {"acknowledged":true}
     2025-08-12T17:11:53.362+08:00 TRACE 13836 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX GET 'http://127.0.0.1:9200/user'
     # HTTP/1.1 200 OK
     # X-elastic-product: Elasticsearch
     # content-type: application/vnd.elasticsearch+json;compatible-with=8
     # Transfer-Encoding: chunked
     #
     # {"user":{"aliases":{},"mappings":{"properties":{"phone":{"type":"integer"}}},"settings":{"index":{"routing":{"allocation":{"include":{"_tier_preference":"data_content"}}},"number_of_shards":"1","provided_name":"user","creation_date":"1754989913010","number_of_replicas":"1","uuid":"9FzPsBRYQ4m2iYvn-2owRQ","version":{"created":"8100099"}}}}}
     查询索引的响应对象:GetIndexResponse: {"user":{"aliases":{},"mappings":{"properties":{"phone":{"type":"integer"}}},"settings":{"index":{"number_of_shards":"1","number_of_replicas":"1","routing":{"allocation":{"include":{"_tier_preference":"data_content"}}},"provided_name":"user","creation_date":1754989913010,"uuid":"9FzPsBRYQ4m2iYvn-2owRQ","version":{"created":"8100099"}}}}}
     2025-08-12T17:11:53.486+08:00 TRACE 13836 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX DELETE 'http://127.0.0.1:9200/user'
     # HTTP/1.1 200 OK
     # X-elastic-product: Elasticsearch
     # content-type: application/vnd.elasticsearch+json;compatible-with=8
     # content-length: 21
     #
     # {"acknowledged":true}
     删除索引的响应对象:DeleteIndexResponse: {"acknowledged":true}
     2025-08-12T17:11:53.491+08:00 TRACE 13836 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX HEAD 'http://127.0.0.1:9200/user'
     # HTTP/1.1 404 Not Found
     # X-elastic-product: Elasticsearch
     # content-type: application/vnd.elasticsearch+json;compatible-with=8
     # content-length: 353
     #
     索引不存在
     */



    @RequestMapping("/create")
    public String createIndex(String indexName) throws Exception {

        return esUtil.createIndex(indexName) ? "索引已存在" : "索引创建成功";
    }



    @RequestMapping("/delete")
    public String deleteIndex(String indexName) {

        boolean hasIndex = esUtil.hasIndex(indexName);

        if (!hasIndex) {
            return "索引不存在或者删除失败";
        }

        return esUtil.deleteIndex(indexName) ? "索引删除成功" : "索引不存在";
    }

    @RequestMapping("/get")
    public String getIndex(String indexName) throws IOException {

        GetIndexRequest getIndexRequest = new GetIndexRequest.Builder().index(List.of("account")).build();

        // GetIndexResponse 封装的是索引数据
        GetIndexResponse getIndexResponse = esClient.indices().get(getIndexRequest);

        // {account=IndexState: {"aliases":{},"mappings":{},"settings":{"index":{"number_of_shards":"1","number_of_replicas":"1","routing":{"allocation":{"include":{"_tier_preference":"data_content"}}},"provided_name":"account","creation_date":1754984916557,"uuid":"dnibJwxzSyumGUmWfNBTXA","version":{"created":"8100099"}}}}}
        System.out.println(getIndexResponse.result());

        return getIndexResponse.result().toString();
    }


    public void test1() throws Exception {

        try {
            FileInputStream fis = new FileInputStream("");
            FileOutputStream fos = new FileOutputStream("");


        } catch (Exception e) {

        }


        FileInputStream fis = new FileInputStream("");
        FileOutputStream fos = new FileOutputStream("");
        try (fis;fos) {

        } catch (Exception e) {

        }
    }

}
