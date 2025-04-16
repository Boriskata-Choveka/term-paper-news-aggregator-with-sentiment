News Aggregator with Sentiment
This repository contains a school project developed as part of my coursework. The application is a simple news aggregator that collects, stores, and analyzes news articles using sentiment analysis. It demonstrates a practical integration of several modern technologies and is intended solely for educational purposes.

Purpose
The main goals of this project are:

Demonstration of Integration: To combine various technologies into one complete application.

Educational Value: To learn and demonstrate the principles of NoSQL databases, containerization, version control, and sentiment analysis.

Data Analysis: To provide an overview of current news trends by grouping articles according to their sentiment (positive, negative, or neutral) and allowing text-based searches.

Technologies Used
Java: The core programming language used for building the application.

MongoDB: A NoSQL database employed for storing news articles along with their sentiment scores.

Docker: Utilized for containerizing both the application and the MongoDB instance, ensuring a consistent development and deployment environment.

Maven: Used as a build and dependency management tool.

Git & GitHub: Employed for version control and collaborative development. The complete project history and codebase are hosted on GitHub.

Features
CRUD Operations: Manage news articles (create, read, update, delete).

Text Search: Perform powerful text-based searches on news content.

Sentiment Analysis: Analyze the content of each news article to determine its sentiment using a basic algorithm that leverages predefined lists of positive and negative words.

Aggregation Queries: Use MongoDB’s aggregation framework to group news by sentiment score, calculate summary statistics (e.g., average sentiment by source or date), and visualize the results in the console.
