<p align="center">
   <img src="https://user-images.githubusercontent.com/36613137/232260367-84fcb344-9384-448a-8afa-79eb2cbb3658.png" />
</p>

# WeatherApp [![Play Store](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/details?id=com.jvrcoding.weatherapp)
Simple application that display current weather in your specific area using [OpenWeather API](https://openweathermap.org/api)

**Language(s):** [Kotlin](https://developer.android.com/kotlin)

## Screenshots
<p align="center">
<img src="https://user-images.githubusercontent.com/36613137/232260479-548b4a8f-095f-4031-aa77-e10146a9af0b.jpg" width="240"> <img src="https://user-images.githubusercontent.com/36613137/232260444-e41503eb-7026-49ad-a8b1-5bf62fb781b1.jpg" width="240"> <img src="https://user-images.githubusercontent.com/36613137/232260509-3c197969-3847-4e51-88af-bfc6622b5565.jpg" width="240"> <img src="https://user-images.githubusercontent.com/36613137/232260514-5382cc4d-0910-402e-89e4-635d79faedcb.jpg" width="240">
</p>

## Project Setup
1. Clone the repository by typing the command below.
```bash
git clone https://github.com/jomarierafa/WeatherApp.git
```
2. In **Android Studio**, select **File** menu and click **Open...**
3. Select the root folder of the project to import.

## Architecture Overview
The project structure follows the MVVM (Model-View-ViewModel) architectural pattern together with clean architecture. You can check GeekforGeeks ***[post](https://www.geeksforgeeks.org/what-is-clean-architecture-in-android)*** for reference.

## Build Versioning
The project follows the following pattern for build versioning:

    majorVersion.releaseVersion.patchNumber (e.g: 1.3.12)

## Library Used
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android’s recommended modern toolkit for building native UI.
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - provides an abstraction layer over SQLite for easier database management.
- [Coil](https://github.com/coil-kt/coil) - image loading library that makes it easy to load images from various sources, such as network, local storage, or even a remote server.
- [Retrofit](https://square.github.io/retrofit/) - simplifies the process of making HTTP network requests.
- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - library for Kotlin programming language that provides a way to write asynchronous, non-blocking code that is both easy to read and easy to write.
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - dependency injection library for Android that is built on top of ***[Google's Dagger 2 library](https://developer.android.com/training/dependency-injection/dagger-android)***.
- [DataStore](https://github.com/VMadalin/easypermissions-ktx) - data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
- [Truth](https://github.com/google/truth) -  testing library for Java and Kotlin that makes it easy to write assertions in tests by providing a fluent and readable API.
- [MockK](https://mockk.io/) - mocking library for Kotlin that provides a simple and powerful API for creating and working with mock objects in tests.
- [Firebase](https://firebase.google.com) - an app development platform that helps you build and grow apps and games users love. Backed by Google and trusted by millions of businesses around the world.

## API Endpoints
The app connects to [OpenWeather API](https://openweathermap.org/api).

*Use to get current weather details.*
```bash
https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}}
``` 
Params:
   > **lat, lon** - geographical coordinates (latitude, longitude). <br>
   > **appid** - your unique API key (you can always find it ***[here](https://home.openweathermap.org/api_keys)***).

## Setup your OpenWeather API key to the project
In order to ensure the security of your API key, it's important to keep it confidential and not expose it publicly. By storing your API key in a local.properties file, you are keeping it separate from your source code and thus preventing accidental exposure or theft of the key.

1. In your Android Studio project, navigate to the root directory of your project.
2. Create a new file called **local.properties** if it doesn't already exist.
3. Open the **local.properties** file and add a new line with the following format: <br>
**api_key=YOUR_API_KEY_HERE**, replacing **YOUR_API_KEY_HERE** with the API key you have.

Note that you may need to clean and rebuild your project after adding the local.properties file to ensure that the key is properly added to your project's resources.
