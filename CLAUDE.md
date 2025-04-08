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

### Manipulators
There are six manipulators on the robot:
- Servos
  - Wrist
    - This operates in a pronation/supination motion
    - 0.0 is pronated
    - 1.0 is supinated
  - Claw
    - This is for grasping the samples
    - 0.0 is open
    - 0.9 is closed
  - Elbow
    - This operates in a flexion/extension motion
    - at 0.275, the arm is parallel to the ground
    - at 0.8 the is about 70 degrees from the ground
  - Rotate
    - This rotates claw
    - 0.5 is square to the robot
    - 1.0 is rotated to the right
    - 0.0 is rotated to the left, but we don't want to go less than 0.2
- Motors
  - Slides
    - Two motors: "right_slide" and "left_slide" (left is reversed)
    - Uses PID control with p=-0.014
    - Position presets:
      - RETRACTED: 0.0 ticks
      - LOW_BASKET: 350.0 ticks
      - HIGH_BASKET: 700.0 ticks
    - Safe range: 0.0 to 700.0 ticks
  - Pivot
    - Two motors: "right_pivot" and "left_pivot" (left is reversed)
    - Uses PID control with p=-0.008
    - Position presets:
      - FLOOR_POSITION: 0.0 ticks
      - SCORING_POSITION: -1000.0 ticks
- Complex Movements
  - These movements describe the combination of servos and motors to achieve a desired motion
  - These movements should be done with a state-machine approach
  - Because the servos move so quickly, and don't offer any position feedback, we need to be careful about the order of operations. Should use millisecond-precise timing to ensure that the servos are in the correct position before moving on to the next step.
  - Travel Position
    - This is the position of the robot when it is moving
    - wrist: 0.75
    - claw:  0.9
    - elbow: 0.75
    - rotate: 0.5
