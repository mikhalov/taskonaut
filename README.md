# Taskonaut
Taskonaut is a powerful note-taking application designed to help users stay organized, focused, and productive. The platform enables users to efficiently create and edit notes, all within a user-friendly interface. Taskonaut ensures secure access with user authentication and offers advanced features for note-taking, organization, and sharing.

## Overview
Taskonaut offers a range of features to assist users in streamlining their note-taking and organization. With user authentication, note-taking capabilities, and sharing options, Taskonaut makes it easy for users to stay organized and collaborate effectively.

## Features

- User Authentication: Taskonaut ensures secure access by implementing JWT token-based authentication in cookies. Users can register, log in, and benefit from input validation for their credentials on both the frontend and backend.

- Note-Taking: Taskonaut's comprehensive note-taking functionality allows users to create, read, edit, and delete notes. Users can also sort and paginate their notes (using the JPA Criteria API for dynamic query creation). Additionally, Taskonaut provides a search feature to find notes by title, content, or label, (leveraging the Criteria API for search with sorting and pagination).

- Note Organization: Users can easily organize their notes using labels, making it simple to categorize and locate specific notes. Taskonaut also offers the ability to delete labels as needed.

- Sharing Notes: Taskonaut provides multiple ways to share notes. Users can download their notes (sorted, like desired) as PDF files or use Telegram Bot to send notes from app or fetch them by label and title from bot. This integration makes it easy to share and access important information, keeping everyone on the same page.

## Current Functionality List

- User Authentication (JWT token in coockies): 
  - Register. 
  - Login.
  - Validation for input credential on backend side(firstly simple validation on frontend side)

- Note-Taking:
    - Create, read, edit, and delete notes.
    - Sorting and paging for notes (uses JPA Criteria API for dinamically query creating).
    - Search notes by title, content or label (also use Criteria API with sorting and paging).
    - Organize notes by labels.
    - Delete labels.
    
- Sharing notes:
    - Download all sorted(optional) notes in PDF.
    - Integrate Telegram API for send notes or get by Label and Title.
    
## The technologies being used along with their short descriptions:

- **Spring Boot Starter Data JPA**: A starter for using Spring Data JPA with Hibernate as the default implementation.
- **JPA Criteria API**: A type-safe Java API for constructing complex queries against a relational database, allowing for dynamic query creation and improved maintainability.
- **Spring Boot Starter Thymeleaf**: A starter for building MVC web applications using Thymeleaf views.

- **Spring Boot Starter Web**: A starter for building web applications, including RESTful APIs, with Spring MVC.

- **Spring Boot Starter Validation**: A starter for using Java Bean Validation with Hibernate Validator as the default implementation.

- **Thymeleaf Extras Spring Security**: An extension that provides Spring Security integration for Thymeleaf templates.

- **Spring Boot Starter Security**: A starter for using Spring Security to secure your web applications and APIs.

- **Flyway Core**: A database migration tool that makes it easy to version control and manage schema changes.

- **PostgreSQL**: A JDBC driver for the PostgreSQL database.

- **Lombok**: A Java library that helps reduce boilerplate code by generating getters, setters, and other common methods.

- **Spring Boot Starter Test**: A starter for testing Spring Boot applications with libraries such as JUnit and Mockito.

- **Java JWT**: A library that provides JSON Web Token (JWT) support for Java applications.

- **Hypersistence Utils**: Used for saving Enums like Types to PostgreSQL DB. A utility library for optimizing Hibernate-based applications.

- **MapStruct**: A code generator that simplifies the implementation of mappings between Java bean types.

- **Hibernate JPA Model Generator**: A library for generating JPA metamodel classes based on your domain model.

- **iText 7**: A PDF library for creating, editing, and processing PDF documents in Java applications.

- **Telegram Bots**: A Java library for building and managing Telegram Bot API integrations.

- **Vanilla JS**: Plain JavaScript without any additional libraries or frameworks, offering simplicity and performance in web development.

- **jQuery**: A popular JavaScript library designed to simplify the client-side scripting of HTML, making it easier to handle events.

- **CSS**: Cascading Style Sheets, a stylesheet language used for describing the look and formatting of a document written in HTML or XML.

- **HTML**: HyperText Markup Language, the standard markup language for creating web pages and web applications, used to structure content on the web.

## Getting Started

These instructions will help you set up the Taskonaut project on your local machine for development and testing purposes.

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- Telegram Bot API token (if using Telegram notifications)

### Installation

1. Clone the repository:
   git clone https://github.com/larbadge/taskonaut.git

2. Navigate to the project directory:
   cd taskonaut

3. Install the dependencies:
   mvn install

4. Configure the `application.properties` file with your database credentials and other necessary settings.

5. Run the application:
   mvn spring-boot:run

The application should now be running at `http://localhost:8080`.

## Contributing

We welcome contributions to the Taskonaut project! Please follow these steps to contribute:

1. Fork the project repository.
2. Create a new branch for your feature or bugfix.
3. Make your changes and commit them to your branch.
4. Push your branch to your forked repository.
5. Create a pull request to merge your changes into the main repository.

Please follow the code style and conventions of the project when making your changes.
