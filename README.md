# liboqs-java

This project is forked from [open-quantum-safe/liboqs-java](https://github.com/open-quantum-safe/liboqs-java). The main difference is that this version uses `liboqs.a` instead of `liboqs.so`, eliminating the need to install liboqs in the system directory. It only requires compiling `liboqs.a` from the `./liboqs/` folder.

## Description
liboqs-java is a Java wrapper for liboqs, providing an interface to quantum-resistant cryptographic algorithms.

## Prerequisites
This code needs to be built in a Linux environment with the following requirements:
- JDK 1.8
- GCC
- CMake
- ninja-build
- Maven
- OpenSSL

## Build Instructions for Linux

1. Clone the repository with submodules
```bash
git clone --recursive https://github.com/AdijeShen/liboqs-java
```

2. Build the liboqs C library to generate liboqs.a
```bash
cd liboqs
cmake -S . -B build
cmake --build build -j4
cd ..
```
This step will generate the `liboqs/build/liboqs.a` file.

3. Build liboqs-java
```bash
mvn package
```
This step will generate `target/liboqs-java.jar` and `target/classes/liboqs-jni.so`.

4. Run the KEM example:
```bash
javac -cp target/liboqs-java.jar examples/KEMExample.java
java -cp target/liboqs-java.jar:examples/ KEMExample
```
