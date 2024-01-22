# Wellbeing Analytics App

## Overview
Wellbeing Analytics is an Android application made for a school project designed to help users track their wellbeing through a series of quizzes. The app collects user responses to questions about their quality of sleep, tiredness, and overall well-being. It stores this data locally and syncs with a server when an internet connection is available.

## Features
- **User Identification:** Users can identify themselves with a name or an existing ID.
- **Quiz Categories:** The app includes three quiz categories - sleep, wellbeing1, and wellbeing2.
- **Daily Quizzes:** The sleep and wellbeing1 quizzes are accessible once a day, while wellbeing2 can be accessed multiple times.
- **Local Database Storage:** User responses are stored locally using Room or SQLite.
- **Data Synchronization:** Data is automatically synced with a server when an internet connection is detected. A manual sync option is also available.
- **Dynamic Questionnaire:** Predefined questions with the flexibility to add more.
- **GUI:** Simple and intuitive graphical user interface.

## Technical Details
- **Language:** Kotlin
- **Database:** Room / SQLite
- **Networking:** OkHttp for server communication
- **Architecture:** MVVM (Model-View-ViewModel)

## Setup and Installation
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the application on an emulator or physical device.

## Usage
- Launch the app and enter a user name or ID.
- Choose a quiz category to start answering questions.
- Submit your answers; they will be saved locally.
- Data will be synced to the server automatically or through the manual sync button when online.

Regarding the server, the use of a dummy server was necessary for this project (hence the special ip `10.0.2.2` which points to localhost on the host device if run on an emulator).

## Contributing
Contributions to the Wellbeing Analytics app are welcome. Please fork the repository and submit a pull request with your changes.

## License
[MIT](https://choosealicense.com/licenses/mit/)