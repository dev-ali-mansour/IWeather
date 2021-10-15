# Android Weather Application

This project is a solution for Android Role assignment for an company.

User can search for a specific city, then the application will search for it's weather data, and cache it for offline usage.
The application require the internet connection while searching for and adding a new city. Then data will be cached on the device.

## Screenshots

<img src="https://i.ibb.co/BgdFk2t/Screenshot-2021-10-08-17-54-15-88-9f89676b8b215e1e3984633e34501759.jpg" width="200"> <img src="https://i.ibb.co/Drr6nKJ/Screenshot-2021-10-08-17-57-56-62-9f89676b8b215e1e3984633e34501759.jpg" width="200"> <img src="https://i.ibb.co/jT06nKt/Screenshot-2021-10-08-17-58-29-55-9f89676b8b215e1e3984633e34501759.jpg" width="200"> <img src="https://i.ibb.co/xFd8Sv7/Screenshot-2021-10-08-17-58-42-11-9f89676b8b215e1e3984633e34501759.jpg" width="200">

## How to use

* First you need to create an account on [OpenWeatherMap](https://www.openweathermap.org)
* Then you need to create an API Key.
* In your local.properties file enter the code below:<br>
apiKey = {YOUR_API_KEY}


## Built With

* [Kotlin](https://kotlinlang.org) - As a programming language.
* [Coroutines](https://developer.android.com/kotlin/coroutines) - For multithreading while handling requests to the server and local database.
* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) - To handle app navigation.
* [Multidex](https://developer.android.com/studio/build/multidex) - To enable creating multi dex files because of using set of libraries that reached the maximum size of single dex file.
* [Model-View-ViewModel(MVVM)](https://developer.android.com/topic/architecture) - Offers an implementation of observer design pattern.
* [Data Binding](https://developer.android.com/topic/libraries/data-binding) - It helps in decoratively binding UI elements of our layout to data source of our app.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notifies views of any database changes in an observer way.
* [Retrofit](https://square.github.io/retrofit/) - It is a type-safe REST client for Android, Java and Kotlin developed by Square. The library provides a powerful framework for authenticating and interacting with APIs and sending network requests with OkHttp.
* [Room DB](https://developer.android.com/training/data-storage/room) - To manage SQLite database easily and avoid a lot boilerplate code.
* [Glide](https://github.com/bumptech/glide) - It is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.
* [Timber](https://github.com/JakeWharton/timber) - It helps in logging while debugging your app. and all logging code will not be embedded in the released APK.
* [Dagger2](https://dagger.dev/) - It is arguably the most used Dependency Injection, or DI, framework for Android. Many Android projects use Dagger to simplify building and providing dependencies across the app. It gives you the ability to create specific scopes, modules, and components, where each forms a piece of a puzzle: The dependency graph.
* [Clean Architecture](https://www.raywenderlich.com/3595916-clean-architecture-tutorial-for-android-getting-started) - Applying Clean Architecture and Solid Principles to build a robust, maintainable, and testable application.
## License
Please review the following [license agreement](https://bumptech.github.io/glide/dev/open-source-licenses.html)
