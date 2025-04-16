package org.example;

import org.bson.Document;
import org.example.daos.ArticleDAO;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String mongoURI = System.getenv("MONGO_URI");
        if (mongoURI == null || mongoURI.isEmpty()) {
            mongoURI = "mongodb://localhost:27017";
        }

        ArticleDAO articleDAO = new ArticleDAO(mongoURI, "newsDB", "articles");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n====== News Aggregator Menu ======");
            System.out.println("1. Insert New Article");
            System.out.println("2. List All Articles");
            System.out.println("3. Create Text Index");
            System.out.println("4. Search Articles by Text");
            System.out.println("5. Recalculate Sentiment Scores");
            System.out.println("6. View Aggregation Results (by Sentiment Score)");
            System.out.println("7. Update Article Sentiment Score");
            System.out.println("8. Delete an Article");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Content: ");
                    String content = scanner.nextLine();
                    System.out.print("Enter Source: ");
                    String source = scanner.nextLine();
                    System.out.print("Enter Sentiment Score (integer): ");
                    int sentimentScore = scanner.nextInt();
                    scanner.nextLine();

                    articleDAO.insertArticle(title, content, source, sentimentScore);
                    break;
                case 2:
                    List<Document> allArticles = articleDAO.getAllArticles();
                    System.out.println("\n--- All Articles ---");
                    for (Document doc : allArticles) {
                        System.out.println(doc.toJson());
                    }
                    break;
                case 3:
                    articleDAO.createTextIndex();
                    break;
                case 4:
                    System.out.print("Enter search keyword/phrase: ");
                    String textQuery = scanner.nextLine();
                    List<Document> foundArticles = articleDAO.searchArticlesByText(textQuery);
                    System.out.println("\n--- Search Results ---");
                    for (Document doc : foundArticles) {
                        String artTitle = doc.getString("title");
                        String artContent = doc.getString("content");
                        if (artContent != null && artContent.length() > 100) {
                            artContent = artContent.substring(0, 100) + "...";
                        }
                        System.out.println("Title: " + artTitle);
                        System.out.println("Content: " + artContent);
                        System.out.println("----------------------");
                    }
                    break;
                case 5:
                    articleDAO.recalculateSentimentForAllArticles();
                    break;
                case 6:
                    List<Document> aggregationResults = articleDAO.aggregateBySentimentScore();
                    System.out.println("\n--- Aggregation Results by Sentiment Score ---");
                    for (Document result : aggregationResults) {
                        System.out.println("Sentiment Score: " + result.get("_id") +
                                " - Count: " + result.get("count"));
                    }
                    break;
                case 7:
                    System.out.print("Enter Article ID to Update: ");
                    String idToUpdate = scanner.nextLine();
                    System.out.print("Enter New Sentiment Score: ");
                    int newScore = scanner.nextInt();
                    scanner.nextLine();
                    articleDAO.updateArticle(idToUpdate, newScore);
                    break;
                case 8:
                    System.out.print("Enter Article ID to Delete: ");
                    String idToDelete = scanner.nextLine();
                    articleDAO.deleteArticle(idToDelete);
                    break;
                case 9:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
        System.out.println("Exiting application.");
    }
}
