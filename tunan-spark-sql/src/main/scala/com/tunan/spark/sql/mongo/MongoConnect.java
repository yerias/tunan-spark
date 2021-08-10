package com.tunan.spark.sql.mongo;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class MongoConnect {

    private static final Logger log = LoggerFactory.getLogger(MongoConnect.class);

//    final static String URL = "39.108.3.210";
//    final static Integer PORT = 27018;
    final static String URL =  "mongodb.xhj.com";
    final static Integer PORT = 27017;

    final static String DB = "cms";
    final static String USER = "readxhjDb";
    final static String PASSWORD = "Xhjreaddb";

    /**
     * mongoClient连接
     */
    protected static MongoClient mongoClient;

    public synchronized static MongoClient getInstance() {
        if (null == mongoClient) {

            ServerAddress address = new ServerAddress(URL, PORT);
            MongoCredential mongoCredential = MongoCredential.createCredential(USER, DB, PASSWORD.toCharArray());
            mongoClient = new MongoClient(address, Collections.singletonList(mongoCredential));

            log.info("mongoClient init success!");
        }
        return mongoClient;
    }

    /**
     * 创建Collection
     *
     * @param dataBaseName   数据库名
     * @param collectionName 集合名
     */
    public void createCollection(String dataBaseName, String collectionName) {
        getDatabase(dataBaseName).createCollection(collectionName);
    }

    /**
     * 查询DataBase
     *
     * @param dataBaseName 数据库名
     * @return
     */
    public MongoDatabase getDatabase(String dataBaseName) {
        return mongoClient.getDatabase(dataBaseName);
    }

    //查询Collection
    public List<String> listCollectionNames(String dataBaseName) {
        List<String> stringList = new ArrayList<String>();
        mongoClient.getDatabase(dataBaseName).listCollectionNames().forEach((Consumer<? super String>) t -> {
            stringList.add(t);
        });
        return stringList;
    }

    //查询指定Collection
    public MongoCollection<Document> getCollectionByName(String dataBaseName, String collectionName) {
        return getDatabase(dataBaseName).getCollection(collectionName);
    }

    //单条删除
    public DeleteResult deleteOne(String dataBaseName, String collectionName, Bson var1) {
        return getCollectionByName(dataBaseName, collectionName).deleteOne(var1);
    }

    //批量删除
    public DeleteResult deleteMany(String dataBaseName, String collectionName, Bson var1) {
        return getCollectionByName(dataBaseName, collectionName).deleteMany(var1);
    }

    //全部删除
    public DeleteResult deleteAll(String dataBaseName, String collectionName) {
        return getCollectionByName(dataBaseName, collectionName).deleteMany(new Document());
    }


    public List<String> createIndex(String collectionName, Map<String,Object> indexMap) {
        List<IndexModel> indexModels = new ArrayList<>();
        for(Map.Entry<String, Object> entry : indexMap.entrySet()){
            indexModels.add(new IndexModel(new BasicDBObject().append(entry.getKey(), entry.getValue())));
        }

        return getCollectionByName(DB, collectionName).createIndexes(indexModels);
    }


    public static void close() {
        if (null != mongoClient) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
