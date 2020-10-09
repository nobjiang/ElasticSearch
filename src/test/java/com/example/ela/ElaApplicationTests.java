package com.example.ela;

import com.alibaba.fastjson.JSON;
import com.example.ela.Utils.JDService;
import com.example.ela.entity.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ElaApplicationTests {

    //两种写法

/*    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient re;*/

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Resource
    JDService parseUtil;


    //7.6.1API

    /*
     * 功能描述:   索引库的创建，删除，查询
     * @Param: []
     * @Return: void
     * @Author: zhaol
     * @Date: 2020/10/8 1:50 下午
     **/

    @Test
    void createIndex() throws IOException {
        //创建索引
        CreateIndexRequest nob = new CreateIndexRequest("jd_goods");
        //执行创建请求IndicesClient,请求后获得响应RequestOptions.DEFAULT  默认请求参数
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(nob, RequestOptions.DEFAULT);

        System.out.println(createIndexResponse);
    }

    //获取索引
    @Test
    void testExist() throws IOException {
        GetIndexRequest nob = new GetIndexRequest("nob");
        //测试获取索引，判断其是否存在
        boolean exists = restHighLevelClient.indices().exists(nob, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //删除索引
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("nob");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete);
    }


    //文档的API


    //创建
    @Test
    void testAddDocument() throws IOException {
        //创建对象
        User user = new User("nob", 3);
        //创建请求
        IndexRequest indexRequest = new IndexRequest("nob");
        //规则：put /xxx/_doc/1
        indexRequest.id("1");
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        //放入数据，Json
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求,获取响应结果
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        System.out.println(index.status());

    }

    //判断文档是否存在
    @Test
    void testGet() throws IOException {
        GetRequest getRequest = new GetRequest("nob", "1");
        //不获取_source的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_node_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //获取文档信息
    @Test
    void getInfo() throws IOException {
        GetRequest getRequest = new GetRequest("nob", "1");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        if (exists) {
            GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            System.out.println(documentFields.getSourceAsString());
            System.out.println(documentFields);
        }


    }

    //更新文档
    @Test
    void uodateRequest() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("nob", "1");
        updateRequest.timeout("1s");
        User user = new User("nob", 100);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    //删除文档
    @Test
    void deleteRequest() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("nob", "1");
        deleteRequest.timeout("1s");
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    //批量插入数据
    @Test
    void testBulk() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        ArrayList<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(new User("nob" + i, i));
        }
        for (int i = 0; i < userList.size(); i++) {
            //add的多个重载方法可以做增删改
            bulkRequest.add(
                    new IndexRequest("nob")
                            .id("" + (i + 1))
                            .source(JSON.toJSONString(userList.get(i)), XContentType.JSON)
            );

        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures()); //false  成功

    }


    //查询
    @Test
    void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询条件，使用QueryBuilder工具实现
        //QueryBuilders.termQuery  精确匹配,如查询name=nob1的用户
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "nob1");
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        //QueryBuilders.matchAllQuery() 匹配所有
        sourceBuilder.query(matchAllQueryBuilder);
        //设置查询超时时间
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        for (SearchHit document : searchResponse.getHits().getHits()) {
            System.out.println(document.getSourceAsMap());
        }

    }


    @Test
    void printjd() throws IOException {
        Boolean java = parseUtil.initData("java");
        System.out.println(java);
    }


    @Test
    void searchPage() throws IOException {
        List<Map<String, Object>> java = parseUtil.searchPage("java", 1, 5);
        System.out.println(JSON.toJSONString(java));
    }



}
