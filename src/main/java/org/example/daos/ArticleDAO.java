package org.example.daos;

import com.mongodb.client.*;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleDAO {
    private MongoCollection<Document> articlesCollection;

    public ArticleDAO(String connectionString, String dbName, String collectionName) {
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(dbName);
        articlesCollection = database.getCollection(collectionName);
    }

    public void insertArticle(String title, String content, String source, int sentimentScore) {
        Document doc = new Document("title", title)
                .append("content", content)
                .append("source", source)
                .append("date", new Date())
                .append("sentimentScore", sentimentScore);
        articlesCollection.insertOne(doc);
        System.out.println("Article inserted!");
    }

    public List<Document> getAllArticles() {
        List<Document> articles = new ArrayList<>();
        try (MongoCursor<Document> cursor = articlesCollection.find().iterator()) {
            while(cursor.hasNext()) {
                articles.add(cursor.next());
            }
        }
        return articles;
    }

    public void updateArticle(String id, int newSentimentScore) {
        ObjectId objId = new ObjectId(id);
        articlesCollection.updateOne(Filters.eq("_id", objId), Updates.set("sentimentScore", newSentimentScore));
        System.out.println("Article updated successfully!");
    }

    public void deleteArticle(String id) {
        ObjectId objId = new ObjectId(id);
        articlesCollection.deleteOne(Filters.eq("_id", objId));
        System.out.println("Article deleted successfully!");
    }
}
