package org.example.daos;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.sort;

public class ArticleDAO {
    private MongoCollection<Document> articlesCollection;

    private static final List<String> POSITIVE_WORDS = Arrays.asList(
            "good", "great", "excellent", "positive", "improved", "up"
    );
    private static final List<String> NEGATIVE_WORDS = Arrays.asList(
            "bad", "poor", "negative", "decline", "down"
    );

    public ArticleDAO(String connectionString, String dbName, String collectionName) {
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(dbName);
        articlesCollection = database.getCollection(collectionName);
    }

    public void createTextIndex() {
        String indexName = articlesCollection.createIndex(
                Indexes.compoundIndex(
                        Indexes.text("title"),
                        Indexes.text("content")
                )
        );
        System.out.println("Text index created: " + indexName);
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

    public List<Document> searchArticlesByText(String searchQuery) {
        List<Document> articles = new ArrayList<>();
        Bson textFilter = Filters.text(searchQuery);
        try (MongoCursor<Document> cursor = articlesCollection.find(textFilter).iterator()) {
            while (cursor.hasNext()) {
                articles.add(cursor.next());
            }
        }
        return articles;
    }

    public int computeSentimentScore(String text) {
        int score = 0;
        if (text == null) {
            return score;
        }

        String lowerText = text.toLowerCase();

        for (String positive : POSITIVE_WORDS) {
            Pattern posPattern = Pattern.compile("\\b" + Pattern.quote(positive) + "\\b");
            if (posPattern.matcher(lowerText).find()) {
                score++;
            }
        }
        for (String negative : NEGATIVE_WORDS) {
            Pattern negPattern = Pattern.compile("\\b" + Pattern.quote(negative) + "\\b");
            if (negPattern.matcher(lowerText).find()) {
                score--;
            }
        }
        return score;
    }

    public void recalculateSentimentForAllArticles() {
        List<Document> articles = getAllArticles();
        for (Document doc : articles) {
            String content = doc.getString("content");
            int newScore = computeSentimentScore(content);
            ObjectId id = doc.getObjectId("_id");
            // Update each document with the new score
            articlesCollection.updateOne(Filters.eq("_id", id), Updates.set("sentimentScore", newScore));
            System.out.println("Updated article " + id.toHexString() + " with new sentiment score: " + newScore);
        }
        System.out.println("Sentiment analysis recalculated for all articles.");
    }

    public List<Document> aggregateBySentimentScore() {
        List<Bson> pipeline = Arrays.asList(
                group("$sentimentScore", sum("count", 1)),
                sort(Sorts.ascending("_id"))
        );
        List<Document> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = articlesCollection.aggregate(pipeline).iterator()) {
            while (cursor.hasNext()) {
                results.add(cursor.next());
            }
        }
        return results;
    }
}
