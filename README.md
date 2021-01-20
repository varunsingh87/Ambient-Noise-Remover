# Ambient Noise Remover

Ambient Noise Remover is a Java program that removes ambient noise from an audio file. It uses the multidimensional Kalman Filter to estimate the positions of the ambient noise in an inputted audio file and the Java Sound API to remove it from the sound file. It then outputs a new file with removed ambient noise. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Ambient-Noise-Remover uses the Maven Dependency Management System to store and update dependencies and version the software. To install Maven, go to their [website](https://maven.apache.org) and download it. 

Run 
```
mvn -v
```

to make sure it is configured correctly. 

### Installing

Install the Maven dependencies for this project

```
mvn install
```

To see the program in action, find background heavy audio files and place them in data. Then edit the file path inside the main method of [AmbientNoiseRemover.java](src/main/java/com/varunsingh/ambientnoiseremover/AmbientNoiseRemover.java)

## Running the tests

To run the tests, enter `mvn test` in the command line. 

### Break down into end to end tests

The AmbientNoiseRemoverTest tests the AmbientNoiseRemover class and is important for identifying the file and its initial sound data

```
@Test
public void getSound_isCorrect() {
    FileInputStream sample1Stream;
    try {
        sample1Stream = new FileInputStream(new File("/data/sample1.mp3"));
        assertEquals(new AmbientNoiseRemover(sample1Stream).getSound().getClass(), Clip.class);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}
```

### Coding Style Tests

Explain what these tests test and why

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

I, Varun Singh, did the research, wrote the software. 

## License

This project is licensed under the GNU General Public License - see the [LICENSE.md](LICENSE.md) file for details

