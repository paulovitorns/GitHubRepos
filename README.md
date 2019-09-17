# GitHubRepos

This is a simple list and search product app using the public 
[API](https://developer.github.com/v3/) from GitHub.

## GitHub API

Since we'll use only public endpoints we don't have to create a project or authenticate our session on this sample.  

## Project architecture

The architecture chosen to this project was the MVP.

Model–view–presenter (MVP) is a derivation of the model–view–controller (MVC) architectural pattern 
which mostly used for building user interfaces. In MVP, the presenter assumes the functionality of 
the “middle-man”. In MVP, all presentation logic is pushed to the presenter. 
MVP advocates separating business and persistence logic out of the Activity and Fragment

> [MVP](https://medium.com/cr8resume/make-you-hand-dirty-with-mvp-model-view-presenter-eab5b5c16e42) Reference.

## Versioning

The versioning scheme follows `major.minor.commit_count`. Major and minor numbers are 
increased manually. Commit count is extracted from the number of commits present in 
the `master` branch. Run this task to check the latest version `./gradlew version`.

## Dependency Injection

[Dagger](https://google.github.io/dagger/) is the tool who help us here. It is a fully static, compile-time 
dependency injection framework for both Java and Android. 
Take at look [here](https://google.github.io/dagger/android.html) if you want to learn more. 

## Code Style

This project is following the Kotlin code style guideline. 
To read more about it just follow it on [Kotlin code style guide](https://android.github.io/kotlin-guides/style.html).

## Linting

Run Kotlin lint:
`./gradlew ktlint`

Run Kotlin lint and apply automatic fixes:
`./gradlew ktlintFormat`

Using [RxLint](https://bitbucket.org/littlerobots/rxlint/src/default/) to make sure our subscribers will 
handle the `onError()` callback and it was added to the current `CompositeDisposable` to avoid memory leaks
or crashes by not dispose the subscription correctly.

## Testing

Run unit tests: 
`./gradlew testDebugUnitTest`
