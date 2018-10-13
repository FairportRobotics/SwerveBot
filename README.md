# Jack Jack

![The Jack Jack of Parr](misc/Jack-Jack.png)

# R&#179;'s First Swervebot

## Features

- Swerves
- Drives

## Notes

- Requires single Logitech F310 controller at port 0
- Left joystick is forward, backwards and strafe
- Right joystick is cc/ccw rotation

## Needs Work

- Back right rotation encoder is not working (bad connection?)
- Encoder seems not that accurate (i.e. adjusting with power level jumps in value)
- How to set everything up when initializing (i.e. 0 is forward? on init, where are the wheels?)
- Field oriented vs human oriented drive
- Understand the encoder settings a little better
- Original code for angle -> enc pos seemed more complex
- Make field oriented work


## Super Helpful Links

- [pdf of operations theory](https://www.chiefdelphi.com/media/papers/download/3028) : https://www.chiefdelphi.com/media/papers/download/3028

- [spreadsheet of operations](https://www.chiefdelphi.com/media/papers/download/3104) : https://www.chiefdelphi.com/media/papers/download/3104

- [general guide](https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/) : https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/
=======
# Jack Jack

![The Jack Jack of Parr](misc/Jack-Jack.png)

# R&#179;'s First Swervebot

## Features

- Swerves
- Drives

## Notes

- Requires single Logitech F310 controller at port 0
- Left joystick is forward, backwards and strafe
- Right joystick is cc/ccw rotation 
- Not Field Oriented (yet)

## Needs Work

- The wheels seem to get off of zero pretty often.
- Don't rotate all the way around on 1000 -> 0
- How to set everything up when initializing (i.e. 0 is forward? on init, where are the wheels?)
- Put encoders on the drive wheels (for motion profiling)
- Field oriented vs human oriented drive
- Understand the encoder settings a little better
- Do the reverse motor instead of rotating the wheel fancy trick
- Get more shuffleboard items displayed.


## Super Helpful Links

- [pdf of operations theory](https://www.chiefdelphi.com/media/papers/download/3028) : https://www.chiefdelphi.com/media/papers/download/3028

- [spreadsheet of operations](https://www.chiefdelphi.com/media/papers/download/3104) : https://www.chiefdelphi.com/media/papers/download/3104

- [general guide](https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/) : https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/
