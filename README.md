# Popular Movies App Stage 2

This is the second part of an app made for Android Developer Nanodegree. The purpose of this app is to allow users to discover the most popular movies playing. It has a few improvements compared to the application made in Stage 1.

The improvements are:
* better layout
* list of favorites

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

You will need to create an account on this link [The Movie DB](https://www.themoviedb.org/) and add the API key on the strings file in the string with name "movie_db_api_key"

### Installing

Clone the github repository

```
$ git clone https://github.com/vinicius1924/Popular-Movies-Stage-2.git
```

Open Android Studio and click: File -> Open and choose the build.gradle file inside the project folder.

After that open the strings file and add the API key in the string with name "movie_db_api_key"

### Branches

**animations**

In this branch I used the MVP pattern, showed in the image below, along with the [dagger](https://github.com/google/dagger) framework for dependency injection and some material design animations

![MVP Pattern](/images/MVP.png)

## Built With

* [gson](https://github.com/google/gson) - A Java serialization/deserialization library to convert Java Objects into JSON and back
* [picasso](http://square.github.io/picasso/) - A powerful image downloading and caching library for Android
* [retrofit](http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java
* [dagger](https://github.com/google/dagger) - A fast dependency injector for Android and Java
* [rxjava](https://github.com/ReactiveX/RxJava) - A library for composing asynchronous and event-based programs by using observable sequences


## Images

![Most Popular Movies](/images/Most_Popular_Movies.png)
![Movie Details](/images/Movie_Details.png)
![Users Reviews](/images/User_Reviews.png)
![Favorites List](/images/Favorites_List.png)
