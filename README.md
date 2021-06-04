# Ambient Noise Remover

Ambient Noise Remover is a Java program that removes ambient noise from an audio file. It uses the multidimensional Kalman Filter to estimate the positions of the ambient noise in an inputted audio file and the Java Sound API to remove it from the sound file. It then outputs a new file with removed ambient noise. 

## Motivation

Communication applications, such as Telegram, Signal, Discord, and Skype, automatically remove background noise during video and voice calls as part of their software, before the sound reaches the recipient. In my project, I wanted to do what they were doing, just in an audio file rather than incoming sound. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Ambient-Noise-Remover uses the Maven Dependency Management System to store and update dependencies and version the software. To install Maven, go to their [website](https://maven.apache.org) and download it. 

#### Maven Dependency Management System

Run 
```
mvn -v
```

to make sure it is configured correctly. 

#### Understanding of the Kalman Filter 

Before writing code for or reading code in this repository, you need to understand how the one dimensional and multidimensional Kalman Filter works. I suggest the following resources:

* [Background Info](https://www.kalmanfilter.net/background.html)
* [One Dimensional Kalman Filter](https://www.kalmanfilter.net/kalman1d.html)
* Multi-Dimensional Kalman Filter: 
        
    - [Video tutorial by Michel van Biezen](https://www.youtube.com/playlist?list=PLX2gX-ftPVXU3oUFNATxGXY90AULiqnWT)
    - [Series of visually-aided articles by Alex Becker](https://www.kalmanfilter.net/kalmanmulti.html)
    - [Math-intensive article on Vector Nav](https://www.vectornav.com/resources/kalman-filters)

### Installing

Install the Maven dependencies for this project

```
mvn install
```

To see the program in action, find background heavy audio files and place them in data. Then edit the file path inside the main method of [AmbientNoiseRemover.java](src/main/java/com/varunsingh/ambientnoiseremover/AmbientNoiseRemover.java)

## Running the Tests

To run the tests, enter `mvn test` in the command line. You can also use the features of your IDE or code editor if it has a GUI for tests. The project currently only has Unit Tests. 

### Unit Tests

The WaveFormTest class tests the WaveForm class and is important for making sure the methods that manipulate the waveform function correctly

```
@Test
public void testInvert() {
    byte[] firstByteArr = { 1, 0, -86, 124 };

    byte[] actualInversion = new WaveForm(firstByteArr).invert().getAudioData();

    assertArrayEquals(new byte[] { -1, 0, 86, -124 }, actualInversion);
}
```

The kalmanfilter test package tests all estimation algorithms from the kalmanfilter package. These are important in making sure the predicted positions and byte data that will be removed is correct.

```
@Test
public void testVelocityStateUpdateEquation() {
    double result = KalmanFilterEquations.useVelocityStateUpdateEquation(
        previousStateEstimate,
        previousVelocityEstimate, 
        BETA_FILTER, 
        measurement, 
        5
    );
    assertEquals(expectedVelocityEstimate, result, 0.1);
}
```

### Coding Style Tests

The Coding Style tests will test to make sure the right method bracket style is used.

```
Give an example
```

## Deployment

Ambient-Noise-Remover is a console program runnable as a JAR file on Windows, Mac, and Linux. There is currently no web version, GUI, or mobile app that hosts, displays, or runs this project. 

## Built With

* [Java](https://www.java.com/en/) - The language used
* [Maven](https://maven.apache.org/) - Dependency Management System
* [Java Sound API](https://www.oracle.com/java/technologies/java-sound-api.html) - The API for manipulating the sound

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to me.

## Versioning

I use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/Borumer/Ambient-Noise-Remover/tags). 

## Author

I, Varun Singh, did the research and wrote the software. 

## License

This project is licensed under the GNU General Public License - see the [LICENSE](LICENSE) file for details

