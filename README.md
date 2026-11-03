# labo3_cross_platform

Kotlin Multiplatform (Compose Multiplatform) project for **Лабораторна робота №3**
(семестр 2, built on top of labs 10-15 of the previous semester).

Targets: **Android**, **Desktop (JVM)**, **iOS** (iOS builds require macOS + Xcode).
Test API: [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com/).

## What Lab 3 changes

### 1. Presentation layer: MVVM -> MVI

`ui/mvi/Mvi.kt` holds the base class `MviViewModel<S : UiState, I : UiIntent>`:

- **Model** - one immutable `UiState` per screen, exposed as `state: StateFlow<S>`;
- **Intent** - a `sealed interface` of everything the user can do; the *only* way into the
  ViewModel is `onIntent(intent)`;
- **View** - the Composable renders `state` and sends intents (`viewModel.onIntent(...)`),
  it never calls ViewModel methods or touches repositories.

State changes only through the reducer `setState { copy(...) }`, so data flows one way:
`View -> Intent -> ViewModel -> new State -> View`.

Every screen now has its own state + intent + ViewModel: `AboutViewModel` (`AboutIntent.Load`),
`RemindersViewModel` (`Add`, `ToggleDone`, `Delete`), `NetworkViewModel` (`Get`, `Post`, `Put`,
`Delete`), `GetTextViewModel` (`Load`), `PostTextViewModel` / `PutTextViewModel` (`Send`),
`DeleteTextViewModel` (`LoadPost`, `Delete`).

### 2. Domain layer: use cases

`domain/usecase/` - one class per application action, each with a single `operator fun invoke`:

- posts: `GetPostUseCase`, `CreatePostUseCase`, `UpdatePostUseCase`, `DeletePostUseCase`,
  `GetPostsTextUseCase`, `CreatePostTextUseCase`, `UpdatePostTextUseCase`, `DeletePostTextUseCase`;
- reminders: `ObserveRemindersUseCase`, `AddReminderUseCase` (the "blank title is rejected,
  input is trimmed" rule now lives here), `ToggleReminderDoneUseCase`, `DeleteReminderUseCase`;
- about: `GetSystemInfoUseCase`, `RegisterAboutVisitUseCase`.

ViewModels depend **only on use cases**, never on repositories. Use cases are registered with
`factoryOf(...)` in the Koin `appModule`.

### 3. Data layer: `map` extension for `NetworkResult`

`data/NetworkResult.kt` gets a public `inline fun <T, R> NetworkResult<T>.map(transform)`
(plus `fold`). `Success` is transformed, `Error` passes through untouched, and an exception
thrown by `transform` becomes `Error`. It is used in the data layer:
`PostRepositoryImpl.deletePostText` turns the service's raw `"HTTP <status>\n\n<body>"` string
into a typed `RawResponse(status, body)`, so no upper layer parses strings anymore.
`NetworkViewModel` also uses it (`deletePost(id).map { "Post #$id deleted." }`).
Unit tests: `commonTest/.../NetworkResultMapTest.kt`.

## Layers

```
ui (Compose Page + MviViewModel)  ->  domain (use cases)  ->  data (repositories, ApiService, DB)
          Intent / State                   invoke()              NetworkResult + map
```

## Run

Android:

```
./gradlew :composeApp:assembleDebug
```

Desktop (JVM):

```
./gradlew :composeApp:run
```

Tests:

```
./gradlew :composeApp:jvmTest
```
