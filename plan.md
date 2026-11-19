
# Smart Lost and Found Desktop Application
## Detailed Responsibilities by Student

---

# Student 1: Backend & Database Lead
### Architecture, Models, Persistence & Business Logic

## 1. Application Architecture & Project Setup

- Establish the overall Java project structure using Maven.
- Configure JavaFX and the required dependencies.
- Design the application architecture and package organization.
- Separate the application into model, DAO, service, controller, and utility layers.
- Define communication between the database, business logic, and JavaFX interface.
- Establish coding conventions and shared interfaces to avoid integration conflicts.

## 2. Object-Oriented Models (`model/`)

- Implement the main application entities:
  - `User`
  - `ItemReport`
  - `LostReport`
  - `FoundReport`
  - `Category`
  - `Claim`
  - `Notification`
  - `Activity`
  - `MatchResult`
- Implement inheritance between `ItemReport`, `LostReport`, and `FoundReport`.
- Apply encapsulation through private attributes, constructors, getters, setters, and validation methods.
- Implement enums for user roles, report types, report statuses, and claim statuses.
- Introduce interfaces and abstract classes where appropriate.
- Ensure consistency between Java models and database structure.

## 3. Database Design & JDBC Integration (`database/`, `dao/`)

- Design the relational database schema using MySQL.
- Create tables for users, categories, item reports, claims, notifications, and activity history.
- Define primary keys, foreign keys, relationships, and constraints.
- Implement the database connection manager using JDBC.
- Implement dedicated DAO classes:
  - `UserDAO`
  - `ItemReportDAO`
  - `CategoryDAO`
  - `ClaimDAO`
  - `NotificationDAO`
  - `ActivityDAO`
- Implement CRUD operations using prepared statements.
- Implement database queries for retrieving reports, user information, claims, and activity history.
- Handle database errors and connection failures.
- Implement password hashing and secure credential handling.
- Prepare database creation scripts and sample data.

## 4. Authentication & Business Logic (`service/`)

- Implement user registration and authentication services.
- Implement role-based authorization for regular users and administrators.
- Ensure users can only modify or delete their own reports unless authorized otherwise.
- Implement item-report creation, modification, deletion, and status management.
- Implement claim submission and claim-status management.
- Implement claim-review authorization and decision recording.
- Prevent unauthorized access to private claim evidence.
- Implement notification creation and read/unread status management.
- Record important report and claim changes in the activity history.
- Ensure that a match score cannot automatically approve a claim.

## 5. JSON Data Management (`json/`, `util/`)

- Integrate Gson or Jackson for JSON serialization and deserialization.
- Implement JSON export for item reports and relevant processing or claim-history summaries.
- Implement JSON import with validation before database insertion.
- Handle malformed JSON, missing fields, invalid values, duplicates, and unsupported versions.
- Exclude passwords, authentication tokens, and private claim evidence from general exports.
- Define and document the JSON structure used by the application.

## 6. Backend Integration & Testing

- Provide reusable service methods for the JavaFX interface.
- Integrate models, database operations, and business logic.
- Coordinate with Student 2 on the methods required by the matching algorithm.
- Test CRUD operations, authorization, database relationships, and JSON functionality.
- Ensure consistent exception handling across the backend.

---

# Student 2: JavaFX & Smart Features Lead
### UI, Matching, Multithreading & Visualization

## 1. JavaFX Application Interface (`view/`, `controller/`)

- Implement the main JavaFX application window and stage.
- Design the application's navigation structure.
- Build the graphical interface using JavaFX layouts, controls, and FXML.
- Use Scene Builder where appropriate.
- Establish a consistent visual theme, including colors, fonts, buttons, icons, and spacing.
- Create reusable UI components for reports, notifications, and status indicators.
- Separate interface logic from business logic.

## 2. Authentication & Item Management Screens

- Design the registration and login interfaces.
- Implement input fields, buttons, validation messages, and error dialogs.
- Design the lost-item and found-item reporting forms.
- Implement fields for:
  - Item title
  - Description
  - Category
  - Color
  - Brand
  - Distinguishing features
  - Date
  - Approximate location
- Implement image selection and preview.
- Create item galleries and report detail screens.
- Display report statuses, creation dates, and last-update dates.
- Implement interfaces for editing and deleting reports.
- Design search and filtering interfaces for keywords, categories, date ranges, locations, colors, and statuses.

## 3. Claims, Notifications & Activity Interfaces

- Design the claim-submission interface.
- Implement private ownership-explanation fields.
- Design the administrator claim-review interface.
- Display claim statuses, reviewer decisions, and decision reasons.
- Implement claim history.
- Design the notification center with read/unread indicators.
- Display possible-match notifications and claim-status updates.
- Create a recent-activity timeline.
- Connect interface elements to the corresponding backend services.

## 4. Smart Matching Engine (`matching/`)

- Design and implement the matching algorithm for comparing lost and found reports.
- Compare multiple attributes:
  - Category similarity.
  - Description and keyword similarity.
  - Color and brand similarity.
  - Date proximity.
  - Location proximity.
- Implement a weighted scoring formula combining the different criteria.
- Normalize individual comparison scores.
- Calculate an overall match score for each candidate.
- Rank potential matches from highest to lowest.
- Implement a `MatchingService` and supporting matching classes.
- Explain which attributes contributed to each match score.
- Display ranked candidates and their matching explanations.
- Refresh potential matches when relevant report information changes.
- Allow users to mark candidates as not matching.
- Ensure matching results remain suggestions and do not automatically determine ownership.

## 5. Multithreading & Background Processing (`tasks/`)

- Implement background tasks for operations that may take significant time.
- Use JavaFX `Task`, `Service`, `ExecutorService`, or `Runnable`.
- Execute matching calculations in background threads.
- Load item images and generate thumbnails asynchronously.
- Calculate dashboard statistics in the background.
- Implement background JSON import/export where appropriate.
- Display progress, completion, failure, and cancellation feedback.
- Use `Platform.runLater()` and JavaFX task callbacks for safe UI updates.
- Ensure JavaFX components are only modified through the JavaFX Application Thread.
- Handle shared data safely between threads.

## 6. Thread-Based Animation (`animation/`)

- Implement at least one animation coordinated with a background task.
- Create an animated scanning or matching indicator while candidate reports are evaluated.
- Synchronize progress indicators with actual background-task progress.
- Implement animated progress bars for JSON processing or statistics generation.
- Use JavaFX animation classes such as:
  - `Timeline`
  - `FadeTransition`
  - `ScaleTransition`
  - `RotateTransition`
- Handle animation completion, failure, and cancellation states.
- Ensure animations never block the JavaFX Application Thread.

## 7. Dashboard & Statistics (`dashboard/`)

- Implement dashboard statistics and visualization.
- Display:
  - Total lost reports.
  - Total found reports.
  - Open claims.
  - Successfully returned items.
  - Reports grouped by category.
  - Reports created over time.
  - Claim approval and rejection statistics.
  - Item return rate.
- Implement JavaFX charts such as `PieChart`, `BarChart`, and `LineChart`.
- Create at least one custom visualization using `Canvas`, `GraphicsContext`, shapes, or graphical calculations.
- Implement administrator filters by date, category, and report status.
- Connect dashboard components to backend statistics services.

## 8. Original Additional Feature

- Propose and implement an original feature extending the mandatory requirements.

**Suggested feature: Smart Match Confidence & Explanation Panel**

- Display a visual breakdown of the matching score.
- Show which attributes contributed positively or negatively.
- Highlight missing or conflicting attributes.
- Explain why a candidate was ranked highly or poorly.
- Integrate the feature into the matching results interface.
- Document its purpose and implementation.

## 9. UI Integration & Testing

- Integrate the JavaFX interface with Student 1's backend services.
- Test navigation, forms, search, and filtering.
- Test matching with successful, unsuccessful, and partially matching candidates.
- Test background tasks and animation coordination.
- Verify that notifications and interface elements refresh when application data changes.
- Ensure the application remains responsive during long-running operations.

---

# Shared Responsibilities (Both Students)

## 1. Planning & Architecture

- Agree on the application architecture and package structure.
- Define the database models and shared method signatures.
- Agree on communication between backend services and JavaFX controllers.
- Design the complete application workflow.

## 2. Integration

- Integrate the backend, database, matching engine, and JavaFX interface.
- Resolve compatibility issues between modules.
- Ensure consistent application behavior and exception handling.
- Test the complete workflow from user registration to successful item return.

## 3. Testing & Quality Assurance

- Test authentication and user permissions.
- Test lost and found report creation and management.
- Test search and matching.
- Test claim submission and review.
- Test JSON import/export.
- Test multithreading and animations.
- Test dashboard statistics and custom graphics.
- Verify that private claim information is protected.

## 4. Documentation & Final Demonstration

- Co-author the four-page technical report.
- Document the application architecture and OOP concepts.
- Explain the database schema and relationships.
- Document the JSON structure.
- Explain the matching algorithm and scoring formula.
- Describe background processing, thread coordination, and animations.
- Document the original additional feature.
- Prepare setup instructions, database scripts, and sample data.
- Prepare the final presentation and demonstration.

---

# Suggested Project Structure

```text
SmartLostAndFound/
│
├── src/main/java/
│   └── com/lostandfound/
│       │
│       ├── Main.java
│       │
│       ├── model/
│       │   ├── User.java
│       │   ├── ItemReport.java
│       │   ├── LostReport.java
│       │   ├── FoundReport.java
│       │   ├── Category.java
│       │   ├── Claim.java
│       │   ├── Notification.java
│       │   └── MatchResult.java
│       │
│       ├── dao/
│       │   ├── DatabaseConnection.java
│       │   ├── UserDAO.java
│       │   ├── ItemReportDAO.java
│       │   ├── ClaimDAO.java
│       │   └── NotificationDAO.java
│       │
│       ├── service/
│       │   ├── AuthenticationService.java
│       │   ├── ItemReportService.java
│       │   ├── ClaimService.java
│       │   └── NotificationService.java
│       │
│       ├── matching/
│       │   ├── MatchingService.java
│       │   ├── MatchingStrategy.java
│       │   └── MatchScorer.java
│       │
│       ├── tasks/
│       │   ├── MatchingTask.java
│       │   ├── StatisticsTask.java
│       │   └── JsonImportTask.java
│       │
│       ├── controller/
│       │   ├── LoginController.java
│       │   ├── DashboardController.java
│       │   ├── ItemController.java
│       │   ├── ClaimController.java
│       │   └── MatchingController.java
│       │
│       ├── dashboard/
│       │   └── CustomChart.java
│       │
│       └── util/
│           ├── JsonManager.java
│           ├── ValidationUtil.java
│           └── ImageUtil.java
│
├── src/main/resources/
│   ├── fxml/
│   ├── css/
│   └── images/
│
├── database/
│   ├── schema.sql
│   └── sample_data.sql
│
├── exports/
│
├── pom.xml
└── README.md
```

---

# Suggested Development Milestones

| Phase | Tasks | Responsible |
|---|---|---|
| 1 | Project setup, architecture, database design | Both |
| 2 | Models, database connection, basic JavaFX interface | Both |
| 3 | CRUD, authentication, and report management | Student 1 |
| 4 | Login, registration, report forms, navigation | Student 2 |
| 5 | Claims, notifications, activity history | Both |
| 6 | Smart matching algorithm | Student 2 |
| 7 | Multithreading and animations | Student 2 |
| 8 | JSON import/export | Student 1 |
| 9 | Dashboard and custom visualization | Student 2 |
| 10 | Integration, testing, documentation, demonstration | Both |

---

# Important Collaboration Rules

To avoid problems during development, establish the following rules from the beginning:

- Student 1 should provide Student 2 with clearly defined service methods instead of requiring direct database access from JavaFX controllers.
- Student 2 should communicate the data and methods needed by the matching engine before implementing it.
- Both students should agree on the `ItemReport`, `Claim`, and `MatchResult` models before implementing dependent features.
- Use Git and separate branches to avoid overwriting each other's work.
- Integrate and test modules regularly instead of waiting until the end.
- Both students should understand the overall application architecture, even when they are not responsible for implementing a particular module.

---

# Final Notes

Student 1 is responsible for the application's foundation, database, persistence, and core business logic.

Student 2 is responsible for the graphical interface, smart matching, multithreading, animations, and dashboard visualizations.
