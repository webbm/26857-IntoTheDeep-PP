# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands
- Build project: `./gradlew build`
- Install to device: `./gradlew installDebug`
- Clean build files: `./gradlew clean`

## Testing
- OpModes are run through the FTC Driver Station app
- Use FTC Dashboard at http://192.168.43.1:8080/dash for telemetry
- PID tuners in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/samples/`

## Code Style
- Kotlin: All classes should be written in Kotlin. Use standard Kotlin conventions (PascalCase for classes, camelCase for functions/variables)
- Java: Dot not write any classes in Java, but do not convert any Java classes to Kotlin unless explicitly told to do so
- Use `@Config` with `public static` fields for FTC Dashboard configuration
- Prefer meaningful descriptive names for all identifiers
- Implement subsystem-based architecture for robot components
- Document tuned PID and servo position values in comments
- Keep related functionality in the same package
- Use error reporting through telemetry for error handling
