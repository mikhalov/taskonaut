# Taskonaut

Taskonaut is a task management and note-taking application that helps users stay organized, focused, and productive. With Taskonaut, you can manage your tasks, create notes, set reminders, and collaborate with others, all in one user-friendly interface.

## Features

- User Authentication: 
  - Register. 
  - Login.
  - Recover passwords.

- Task Management:
    - Create, read, update, and delete tasks.
    - Prioritize tasks (low, medium, high).
    - Set deadlines and reminders.
    - Categorize tasks (e.g., personal, work, school).
    - Share tasks with other users or assign tasks to them.
- Note-Taking:
    - Create, read, update, and delete notes.
    - Organize notes into notebooks or folders.
    - Support rich text formatting.
    - Add images, attachments, or embedded media to notes.
    - Share notes with other users or collaborate in real-time.
- Notifications and Alerts:
    - Send email or push notifications for deadlines and reminders.
    - Integrate Telegram API for notifications and reminders.
- User Preferences and Settings:
    - Customize the app's theme, colors, and fonts.
    - Choose date and time formats.
    - Configure notification preferences.

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

4. Configure the `application.properties` or `application.yml` file with your database credentials and other necessary settings.

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
