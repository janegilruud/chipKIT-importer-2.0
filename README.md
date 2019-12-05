# MPLAB Arduino Import plugin
> A wizard that allows importing Arduino projects into MPLAB X IDE

This plugin contains an import wizard that can be launched from File->Import->Import Arduino Project in the MPLAB X IDE.
The wizard will allow you to select a sketch file (*.ino) that was created in Arduino IDE.
Next, it will convert the sketch into an MPLAB X Makefile project.
With this project you will be able to build and debug the Arduino sketch in MPLAB X IDE.
Currently it supports AVR and chipKIT boards.

A complete set of Help documentation is included.
Note that the wizard can import a project in two ways:
1) Copying all source code and libraries, in order to create a stand-alone MPLAB X project which is logically separate from the original (default mode) or
2) Retaining links to external source code and libraries that exist in the Arduino environment.

The Arduino Import Plugin requires the following software to be installed:
- MPLAB X v5.25 or later
- Arduino IDE v1.8.5 or later
- Relevant toolchains configured in MPLAB X

## Installing / Getting started

A quick introduction of the minimal setup you need to get a hello world up &
running.

## Developing

Here's a brief intro about what a developer must do in order to start developing
the project further.

### Building

The Plugin is a NetBeans Module project so the easiest way to compile it is to clone (or download) the project and open it in Netbeans IDE (at least version 8.0). You will need to specify the MPLAB X IDE as the target platform as the project depends on many plugins from this platform. In order to do that:
- Go to Tools -> Netbeans Platforms in main application menu;
- Click the Add Platform button at the bottom of the popup window;
- Navigate to the directory called mplab_ide in the MPLAB X IDE install directory.

After adding the platform to your development environment, the last thing to do is to specify that the chipKIT Import Plugin should use the MPLAB X IDE platform instead of the default one so:
- Right-click on the project to open the context menu;
- Go to Properties (last item on the menu);
- In the Categories section select Libraries (second from the top);
- From the NetBeans Platform drop down menu select the MPLAB X IDE platform that you have added earlier.

At this point, you should be able to build the Plugin just like any other project in NetBeans (Select Run -> Build Project in the main menu or click the button with a hammer icon in the toolbar).

### Deploying / Publishing

In case there's some step you have to take that publishes this project to a
server, this is the right time to state it.

## Features

What's all the bells and whistles this project can perform?
* What's the main functionality
* You can also do another thing
* If you get really randy, you can even do this

## Configuration

Here you should write what are all of the configurations a user can enter when
using the project.

## Contributing

Here you should write how to contribute to this project.

