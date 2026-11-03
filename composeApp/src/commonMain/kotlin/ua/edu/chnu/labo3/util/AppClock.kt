package ua.edu.chnu.labo3.util

/**
 * Small platform-agnostic time helper.
 *
 * The shared code only needs two things for the About-screen statistics:
 * the current moment (to record when the screen was opened) and a way to turn
 * a stored moment into a human-readable string. Each target supplies the
 * implementation with its own date/time API.
 */
expect fun nowEpochMillis(): Long

/** Formats an epoch-millis moment as a local `dd.MM.yyyy HH:mm:ss` string. */
expect fun formatEpochMillis(epochMillis: Long): String
