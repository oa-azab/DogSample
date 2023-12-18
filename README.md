# Dog Sample
Sample app that gets random dog images from https://thedogapi.com/

The app fetch random dog images and its metadata from api and cache them in memory.
Main screen shows number of random dog images, user can swipe down to refresh the list that will clear cache and fetch fresh images from api.
Details screen shows more information about dog breed.


|branch  | UI architcure |
|--|--|
| [master](https://github.com/oa-azab/DogSample/tree/master) | MVP |
| [viewmodel](https://github.com/oa-azab/DogSample/tree/viewmodel) | MVVM |

# Architecture

App has 3 Main Packages (Data, Domain, UI) if the app is more complex and these packages could separate modules.

## Data

This package where repositories and data sources live, the classes are responsible for read/write data from sources and map them to app models

## Domain

This package contain business logic of the app functionality.
consist of number of UseCases each on serve business function and should return simple result to be displayed by UI layer.


## UI
in this app the UI layer could be one of:
- MVP the presenter contains the ui logic and depend on number of usescases and forward the result to the View to display it
- MVVM the ViewModel contains the ui logic and it expose the UI state to the view to display it.


# Dependency Injection
Dagger will be responsible for creating dependencies up to the UseCases and will provide all available UseCases in AppComponent 