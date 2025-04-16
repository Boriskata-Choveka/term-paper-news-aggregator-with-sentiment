package org.example;

import org.example.daos.ArticleDAO;

import org.bson.Document;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String mongoURI = System.getenv("MONGO_URI");
        if (mongoURI == null || mongoURI.isEmpty()) {
            mongoURI = "mongodb://localhost:27017";
        }

        // Initialize ArticleDAO with the given connection string, database, and collection.
        ArticleDAO articleDAO = new ArticleDAO(mongoURI, "newsDB", "articles");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n====== News Aggregator Menu ======");
            System.out.println("1. Insert New Article");
            System.out.println("2. List All Articles");
            System.out.println("3. Update Article Sentiment Score");
            System.out.println("4. Delete an Article");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear the newline

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
                    scanner.nextLine();  // clear newline

                    articleDAO.insertArticle(title, content, source, sentimentScore);
                    break;
                case 2:
                    System.out.println("Listing All Articles:");
                    List<Document> articles = articleDAO.getAllArticles();
                    for (Document doc : articles) {
                        System.out.println(doc.toJson());
                    }
                    break;
                case 3:
                    System.out.print("Enter Article ID to Update: ");
                    String idToUpdate = scanner.nextLine();

                    System.out.print("Enter New Sentiment Score: ");
                    int newScore = scanner.nextInt();
                    scanner.nextLine(); // clear newline

                    articleDAO.updateArticle(idToUpdate, newScore);
                    break;
                case 4:
                    System.out.print("Enter Article ID to Delete: ");
                    String idToDelete = scanner.nextLine();
                    articleDAO.deleteArticle(idToDelete);
                    break;
                case 5:
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