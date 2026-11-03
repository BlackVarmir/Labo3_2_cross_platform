# labo3_cross_platform

Kotlin Multiplatform (Compose Multiplatform) project for **Лабораторна робота №15**.
Continuation of Lab 14 - adds a **DELETE request whose result is shown on screen as plain text**,
on top of the **Ktor** + **Koin** networking layer introduced in Lab 11.

Targets: **Android**, **Desktop (JVM)**, **iOS** (iOS builds require macOS + Xcode).

Test API: [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com/).

## What Lab 15 adds

1. **Raw DELETE call** - `ApiService.deletePostRawText(id)` performs `DELETE /posts/{id}` and
   returns the HTTP status line plus the response body verbatim via Ktor's `bodyAsText()`,
   without deserializing it (jsonplaceholder answers with `200 OK` and an empty `{}` body).
2. **Repository method** - `PostRepository.deletePostText(id)` wraps that call in a
   `NetworkResult` so success and failure are handled uniformly.
3. **"DELETE as text" screen** (`ui/deletetext/`) - `DeleteTextPage` + `DeleteTextViewModel`.
   Step 1: on open, the screen loads post id = 1 with GET and shows what will be deleted.
   Step 2: nothing is deleted automatically - the DELETE is sent only when the user presses
   **"Delete post #1"**. A progress indicator is shown while the call is in flight; then the
   post is marked as deleted, a confirmation is shown, and the raw server response (request,
   HTTP status and body) is rendered on screen as plain text (or an error message on failure).
   **"Start over"** reloads the post. Note: jsonplaceholder is a fake API - it answers
   `200 OK` but does not really delete anything, so the post comes back after a reload.
4. **Navigation** - new `Screen.DeleteText` route, a Home-screen button, and the matching
   `composable` entry in `AppNavHost`; `DeleteTextViewModel` is registered in the Koin `appModule`.

## Carried over from earlier labs

- The Lab 14 **"PUT as text"** screen (`ui/puttext/`), Lab 13 **"POST as text"** screen
  (`ui/posttext/`) and Lab 12 **"GET as text"** screen (`ui/gettext/`), Ktor client +
  kotlinx.serialization networking layer and `NetworkResult` (Lab 11), the GET/POST/PUT/DELETE
  test screen (`ui/network/`), custom theme (`ui/theme/`, `labo3Theme`), Koin DI,
  Multiplatform Settings (About-screen visit stats), SQLDelight reminders, Kermit logging, and
  `expect class Platform` for system info.

## Run

Android:

```
.\gradlew.bat :composeApp:assembleDebug
```

Desktop (JVM):

```
.\gradlew.bat :composeApp:run
```

iOS: open the project on macOS and run the `iosApp` target.

> Note: this project was set up on Windows. Android and Desktop targets build on Windows;
> the iOS target requires macOS. Android requires the `INTERNET` permission (already declared
> in the manifest).
