package com.xiao.springbootelasticsearchdemo.controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.xiao.springbootelasticsearchdemo.utils.ESUtil;
import com.xiao.springbootelasticsearchdemo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 文档操作
 */
@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private ESUtil esUtil;

    @Autowired
    private ElasticsearchClient esClient;

    @RequestMapping("/operation")
    public void operation() throws IOException {
        String indexName = "user";
        BooleanResponse exists = esClient.indices().exists(req -> req.index(indexName));
        if (!exists.value()) {
            CreateIndexResponse createIndexResponse = esClient.indices().create(req -> req.index(indexName));
        }

        User user = new User("张三", 19, "10086");

        // 创建文档
        CreateRequest<User> createRequest = new CreateRequest.Builder<User>()
                .index(indexName)
                .id("1")
                .document(user)
                .build();
        CreateResponse createResponse = esClient.create(createRequest);
        System.out.println("文件创建的响应对象:" + createResponse);

        // 获取文档
        GetRequest getRequest = new GetRequest.Builder().index(indexName).id("1").build();
        GetResponse<User> getResponse = esClient.get(getRequest, User.class);
        System.out.println("获取文档的响应对象:" + getResponse);

        // 删除文档
        DeleteRequest deleteRequest = new DeleteRequest.Builder().index(indexName).id("1").build();
        DeleteResponse deleteResponse = esClient.delete(deleteRequest);
        System.out.println("删除文档的响应对象:" + deleteResponse);

        // 获取文档
        GetRequest getRequest2 = new GetRequest.Builder().index(indexName).id("1").build();
        GetResponse<User> getResponse2 = esClient.get(getRequest2, User.class);
        System.out.println("获取文档的响应对象:" + getResponse2);

    }

/*
 # HTTP/1.1 404 Not Found
 # X-elastic-product: Elasticsearch
 # content-type: application/vnd.elasticsearch+json;compatible-with=8
 # content-length: 353
 #
 2025-08-12T17:31:55.380+08:00 TRACE 32420 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX PUT 'http://127.0.0.1:9200/user' -d '{}'
 # HTTP/1.1 200 OK
 # X-elastic-product: Elasticsearch
 # content-type: application/vnd.elasticsearch+json;compatible-with=8
 # content-length: 63
 #
 # {"acknowledged":true,"shards_acknowledged":true,"index":"user"}
 2025-08-12T17:31:55.477+08:00 TRACE 32420 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX PUT 'http://127.0.0.1:9200/user/_create/1' -d '{"name":"张三","age":19,"phone":"10086"}'
 # HTTP/1.1 201 Created
 # Location: /user/_doc/1
 # X-elastic-product: Elasticsearch
 # content-type: application/vnd.elasticsearch+json;compatible-with=8
 # content-length: 137
 #
 # {"_index":"user","_id":"1","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}
 文件创建的响应对象:CreateResponse: {"_id":"1","_index":"user","_primary_term":1,"result":"created","_seq_no":0,"_shards":{"failed":0.0,"successful":1.0,"total":2.0},"_version":1}
 2025-08-12T17:31:55.538+08:00 TRACE 32420 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX GET 'http://127.0.0.1:9200/user/_doc/1'
 # HTTP/1.1 200 OK
 # X-elastic-product: Elasticsearch
 # content-type: application/vnd.elasticsearch+json;compatible-with=8
 # content-length: 136
 #
 # {"_index":"user","_id":"1","_version":1,"_seq_no":0,"_primary_term":1,"found":true,"_source":{"name":"张三","age":19,"phone":"10086"}}
 获取文档的响应对象:GetResponse: {"_index":"user","found":true,"_id":"1","_primary_term":1,"_seq_no":0,"_source":"User(name=张三, age=19, phone=10086)","_version":1}
 2025-08-12T17:31:55.589+08:00 TRACE 32420 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX DELETE 'http://127.0.0.1:9200/user/_doc/1'
 # HTTP/1.1 200 OK
 # X-elastic-product: Elasticsearch
 # content-type: application/vnd.elasticsearch+json;compatible-with=8
 # content-length: 137
 #
 # {"_index":"user","_id":"1","_version":2,"result":"deleted","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":1,"_primary_term":1}
 删除文档的响应对象:DeleteResponse: {"_id":"1","_index":"user","_primary_term":1,"result":"deleted","_seq_no":1,"_shards":{"failed":0.0,"successful":1.0,"total":2.0},"_version":2}
 2025-08-12T17:31:55.594+08:00 TRACE 32420 --- [springboot-elasticsearch-demo] [nio-8899-exec-1] tracer                                   : curl -iX GET 'http://127.0.0.1:9200/user/_doc/1'
 # HTTP/1.1 404 Not Found
 # X-elastic-product: Elasticsearch
 # content-type: application/vnd.elasticsearch+json;compatible-with=8
 # content-length: 41
 #
 # {"_index":"user","_id":"1","found":false}
 获取文档的响应对象:GetResponse: {"_index":"user","found":false,"_id":"1"}
 */



    @RequestMapping("/query")
    public void query() throws IOException {
        String indexName = "user";
        BooleanResponse exists = esClient.indices().exists(b -> b.index(indexName));
        if (!exists.value()) {
            CreateIndexResponse createIndexResponse = esClient.indices().create(req -> req.index(indexName));
        }

//        // 批量创建文档
//        List<User> users = Arrays.asList(
//                new User("张三", 15, "10016"),
//                new User("张三", 20, "10026"),
//                new User("张三", 25, "10036"),
//                new User("张三", 30, "10046"),
//                new User("张三", 35, "10056")
//        );
//
//        List<BulkOperation> operations = new ArrayList<>();
//        for (int i = 0; i < users.size(); i++) {
//            User user = users.get(i);
//            CreateOperation<User> createOp = new CreateOperation.Builder<User>()
//                    .index(indexName)
//                    .id(String.valueOf(i + 1))
//                    .document(user)
//                    .build();
//            operations.add(new BulkOperation.Builder().create(createOp).build());
//        }
//
//        BulkRequest bulkRequest = new BulkRequest.Builder()
//                .operations(operations)
//                .build();
//
//        // 执行批量操作
//        BulkResponse bulkResponse = esClient.bulk(bulkRequest);
//        System.out.println("文档批量创建的响应对象:" + bulkResponse);

        // 1. 构建查询条件
        // 构建 match 查询
        MatchQuery matchQuery = new MatchQuery.Builder()
                .field("name")
                .query("张三")
                .fuzziness("AUTO")
                .build();
        Query matchQueryWrapper = new Query.Builder().match(matchQuery).build();

        // 构建range查询
        RangeQuery rangeQuery = new RangeQuery.Builder()
                .field("age")
                .gte(JsonData.of(20))
                .lte(JsonData.of(30))
                .build();
        Query rangeQueryWrapper = new Query.Builder().range(rangeQuery).build();

        // 构建bool查询
        BoolQuery boolQuery = new BoolQuery.Builder()
                .must(matchQueryWrapper)
                .filter(rangeQueryWrapper)
                .build();
        Query finalQuery = new Query.Builder().bool(boolQuery).build();

        // 2. 构建排序条件
        SortOptions sortOptions = new SortOptions.Builder()
                .field(new FieldSort.Builder()
                        .field("age")
                        .order(SortOrder.Desc)
                        .build())
                .build();

        // 3. 构建聚合条件
        // 构建terms聚合
        TermsAggregation termsAggregation = new TermsAggregation.Builder()
                .field("age")
                .size(10) // 10: 返回查询到的文档数据  0: 不返回查询到的文档数据
                .build();

        Aggregation ageAggregation = new Aggregation.Builder()
                .terms(termsAggregation)
                .build();

        // 查询文档：age 在 20 到 30 之间的，并按照 age 分组
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(indexName)
                .query(finalQuery)
                .sort(sortOptions)
                .from(0)
                .size(10)
                .aggregations("age_stats", ageAggregation)
                .build();
        SearchResponse<User> searchResponse = esClient.search(searchRequest, User.class);
        System.out.println("查询响应对象数据:" + searchResponse);
    }

/*
查询响应对象数据: SearchResponse:
{
    "took": 19,
    "timed_out": false,
    "_shards": {
        "failed": 0.0,
        "successful": 1.0,
        "total": 1.0,
        "skipped": 0.0
    },
    "hits": {
        "total": {
            "relation": "eq",
            "value": 3
        },
        "hits": [
            {
                "_index": "user",
                "_id": "4",
                "_score": null,
                "_source": "User(name=张三, age=30, phone=10046)",
                "sort": [
                    30
                ]
            },
            {
                "_index": "user",
                "_id": "3",
                "_score": null,
                "_source": "User(name=张三, age=25, phone=10036)",
                "sort": [
                    25
                ]
            },
            {
                "_index": "user",
                "_id": "2",
                "_score": null,
                "_source": "User(name=张三, age=20, phone=10026)",
                "sort": [
                    20
                ]
            }
        ],
        "max_score": null
    },
    "aggregations": {
        "lterms#age_stats": {
            "buckets": [
                {
                    "doc_count": 1,
                    "key": 20
                },
                {
                    "doc_count": 1,
                    "key": 25
                },
                {
                    "doc_count": 1,
                    "key": 30
                }
            ],
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0
        }
    }
}
 */
}
