# liboqs-java

This project is forked from [open-quantum-safe/liboqs-java](https://github.com/open-quantum-safe/liboqs-java). The main difference is that this version uses `liboqs.a` instead of `liboqs.so` or `liboqs.dll`, eliminating the need to install liboqs in the system directory. It only requires compiling `liboqs.a` from the `./liboqs/` folder.

## Windows Build

### Prerequisites
- MinGW-w64 GCC (version 11.5.0 or later)
- CMake
- JDK 1.8
- Maven 3.8.8
- Git

### Installation Steps

1. Install MinGW-w64 GCC:
- Download from [WinLibs](https://winlibs.com/#download-release)
- Extract the ZIP file to a directory without spaces
- Add the bin directory to PATH environment variable (e.g., `E:\develop\mingw64\bin`)
  - Via Control Panel → System → System Info → Advanced System Settings → Advanced → Environment Variables → PATH
  - Or via command line: `setx PATH "E:\develop\mingw64\bin;%PATH%"` (not recommended)

2. Install CMake:
- Either via winget: `winget install cmake`
- Or download from [cmake.org](https://cmake.org/download/)
- Ensure CMake is added to PATH

3. Verify installations:
```bash
gcc --version
cmake --version
```

4. Clone the repository:
```bash
git clone --recursive https://github.com/AdijeShen/liboqs-java
```

5. Build liboqs:
```bash
cd liboqs
cmake -G "MinGW Makefiles" -DCMAKE_C_COMPILER=gcc -DBUILD_SHARED_LIBS=OFF -S . -B build
cmake --build build -j 4
cmake --install build
cd ..
```

6. Install Java dependencies:
- Install JDK 1.8 from [OpenLogic](https://www.openlogic.com/openjdk-downloads)
- Install Maven 3.8.8 from [Maven](https://maven.apache.org/)
- Add both to PATH environment variables

7. Verify Java installations:
```bash
java -version
mvn -version
```

8. Build the package:
```bash
mvn package -P windows
```

9. Run the KEM example:
```bash
javac -cp target/liboqs-java.jar examples/KEMExample.java
java -cp "target/liboqs-java.jar;examples/" KEMExample
```

Note: For Windows paths in Java commands, use semicolon (;) as the path separator instead of colon (:) used in Linux.

## linux build

### Prerequisites
This code needs to be built in a Linux environment with the following requirements:
- JDK 1.8
- GCC
- CMake
- ninja-build
- Maven
- OpenSSL

### Build Instructions for Linux

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
mvn package -P linux
```
This step will generate `target/liboqs-java.jar` and `target/classes/liboqs-jni.so`.

4. Run the KEM example:
```bash
javac -cp target/liboqs-java.jar examples/KEMExample.java
java -cp target/liboqs-java.jar:examples/ KEMExample
```

## How to use liboqs-java

Check this repo out for how to use liboqs-java in your project: [oqs-java-kem-test](https://github.com/AdijeShen/oqs-java-kem-test)
