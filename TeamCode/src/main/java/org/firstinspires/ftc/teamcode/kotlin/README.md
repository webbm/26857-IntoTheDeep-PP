# Kotlin Support for FTC

This directory contains Kotlin examples and support files for the FTC project.

## Kotlin Version

The project uses Kotlin version 1.9.20, which is compatible with:
- Java 8 (the project's current Java compatibility level)
- Gradle 8.9
- Android Gradle Plugin 8.7.0

## File Structure

- `/kotlin/`: Directory for organizing Kotlin-specific files
  - `RobotHardwareKt.kt`: Example hardware class in Kotlin
- `/KotlinExample.kt`: Example OpMode written in Kotlin

## Usage

You can write new OpModes in Kotlin by:
1. Creating a new file with a `.kt` extension
2. Extending the appropriate OpMode class (`LinearOpMode`, `OpMode`, etc.)
3. Annotating it with `@TeleOp` or `@Autonomous` as you would in Java

## Java Interoperability

Kotlin classes can seamlessly use Java classes, and Java classes can use Kotlin classes. This allows for gradual migration or using both languages side by side.

## Example

```kotlin
@TeleOp(name = "Kotlin Example", group = "Examples")
class KotlinExample : LinearOpMode() {
    // Kotlin code here
    override fun runOpMode() {
        // Implementation
    }
}
```

## Notes

- When accessing enums from Java, you may need to use the full package name (e.g., `com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD`)
- Kotlin provides many features to reduce boilerplate code and improve safety