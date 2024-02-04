# Time Calc

## Introduction

Time Calc is a desktop application used to track the remaining time until the end of some activity - like working hours.

_Time Calc is written in Java programming language and uses the Swing framework._

![Tux, the Linux mascot](images/screenshot.png)
## Usage

### Start of application

When "Time Calc" is started", user is asked for:
 - start time ... like 7:30
 - overtime ... like 0:45 ... overtime is optional and the default value is 0:00

### Restart of application

You can restart the app, if you press the **"Restart"** button.
 - Then you are asked again for start time and overtime.

### End of application

You can stop the app, if you press the **"Exit"** button or click on the exit window button.
- Then application is stopped.


## Special files

If these files are present, something special happens.

### starttime.txt

This file contains the default start time - used during the previous run of the app. 
If file starttime.txt does not exist, then the default start time is 7:00.

### overtime.txt

This file contains the default overtime - used during the previous run of the app.
If file overtime.txt does not exist, then the default overtime is 0:00.

### test.txt
If file test.txt exists, then user is not asked for start time and overtime. Instead, the values in files starttime.txt and overtime.txt are used.

## Key shortcuts

## Command button

## Todos

 * Config window
 * Split to Maven modules
 * Junit, Mockito, etc.
 * Checkstyle
 * Sonarlint
 * Sonarqube
 * Add SQLite support and store times of arrivals and departures and time of activities

