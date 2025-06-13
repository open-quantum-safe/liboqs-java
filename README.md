[![GitHub Actions Build Status](https://github.com/open-quantum-safe/liboqs-java/actions/workflows/java.yml/badge.svg)](https://github.com/open-quantum-safe/liboqs-java/actions/workflows/java.yml)

# liboqs-java: Java wrapper for liboqs

**liboqs-java** offers a Java wrapper providing quantum-resistant cryptographic algorithms via [liboqs](https://github.com/open-quantum-safe/liboqs/).


## Overview

The **Open Quantum Safe (OQS) project** has the goal of developing and prototyping quantum-resistant cryptography.

**liboqs** is an open source C library for quantum-resistant cryptographic algorithms. See more about liboqs at https://github.com/open-quantum-safe/liboqs, including a list of supported algorithms.

**liboqs-java** is an open source Java wrapper for the liboqs C library that provides:
* a common API for post-quantum key encapsulation mechanisms and digital signature schemes
* a collection of open source implementations of post-quantum cryptography algorithms

The OQS project also provides prototype integrations into application-level protocols to enable testing of quantum-resistant cryptography.

More information on OQS can be found on https://openquantumsafe.org.



## liboqs-java

This solution implements a Java wrapper for the C OQS library. It contains the following directories:

* __`src/main/c/`:__ Native C JNI wrapper code that interfaces with liboqs.

* __`src/main/java/org/openquantumsafe/`:__  Java wrappers for the liboqs C library.

* __`src/test/java/org/openquantumsafe/`:__  Unit tests.

* __`examples/`:__  Key encapsulation, digital signatures and rand examples.


`liboqs-java` defines four main classes: **`KeyEncapsulation`** and **`Signature`**, providing post-quantum key encapsulation and signature mechanisms, respectively, and **`KEMs`** and **`Sigs`**, containing only static member functions that provide information related to the available key encapsulation mechanisms or signature mechanism, respectively.

`KeyEncapsulation` and/or `Signature` must be instantiated with a string identifying one of mechanisms supported by liboqs; these can be enumerated using the `KEMs.get_enabled_KEMs()` and `Sigs.get_enabled_sigs()` methods.

Support for alternative RNGs is provided via the `randombytes` functions.

The examples in the [examples](./examples/) directory are self-explanatory and provide more details about the wrapper's API.



## Limitations and security
`liboqs` and `liboqs-java` are designed for prototyping and evaluating quantum-resistant cryptography. Security of proposed quantum-resistant algorithms may rapidly change as research advances, and may ultimately be completely insecure against either classical or quantum computers.

We believe that the NIST Post-Quantum Cryptography standardization project is currently the best avenue to identifying potentially quantum-resistant algorithms. `liboqs` does not intend to "pick winners", and we strongly recommend that applications and protocols rely on the outcomes of the NIST standardization project when deploying post-quantum cryptography.

We acknowledge that some parties may want to begin deploying post-quantum cryptography prior to the conclusion of the NIST standardization project. We strongly recommend that any attempts to do make use of so-called hybrid cryptography, in which post-quantum public-key algorithms are used alongside traditional public key algorithms (like RSA or elliptic curves) so that the solution is at least no less secure than existing traditional cryptography.

`liboqs-java` is provided "as is", without warranty of any kind. See [LICENSE](./LICENSE) for the full disclaimer.



## Building
Builds are tested in GitHub Actions on Linux (Ubuntu 24.04 with OpenJDK 21) and macOS ([macos-latest runner](https://docs.github.com/en/actions/using-github-hosted-runners/using-github-hosted-runners/about-github-hosted-runners#standard-github-hosted-runners-for-public-repositories), at macOS Sonoma with Java 21 as of liboqs-java 0.2.0 release).

### Pre-requisites
To build the Java OQS wrapper you need a Java Development Kit (JDK), such as [OpenJDK](https://openjdk.java.net/) >= 8 and [Apache Maven](https://maven.apache.org/).

To build `liboqs-java` first download or clone this java wrapper into a `liboqs-java` folder, e.g.,

```
git clone -b master https://github.com/open-quantum-safe/liboqs-java.git
```

### Windows Build

#### Prerequisites

- MinGW-w64 GCC (version 11.5.0 or later)
- CMake
- JDK 1.8
- Maven 3.8.8
- Git

#### Installation Steps

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

3. Verify installations (by open cmd and type):
```bash
gcc --version
cmake --version
```

4. Build liboqs:
```bash
git clone https://github.com/open-quantum-safe/liboqs/
cmake -G "MinGW Makefiles" -DCMAKE_C_COMPILER=gcc -DBUILD_SHARED_LIBS=OFF -S . -B build
cmake --build build -j4
cd ..
```

5. Install Java dependencies:
- Install JDK 1.8 from [OpenLogic](https://www.openlogic.com/openjdk-downloads)
- Install Maven 3.8.8 from [Maven](https://maven.apache.org/)
- Add both to PATH environment variables
- Verify Java installations:
```bash
java -version
mvn -version
```

If you clone the liboqs under `liboqs-java` directory, then you can run the following command to build the package:
```bash
mvn package -P windows
```

Or else, you should run
```bash
mvn package -P windows -Dliboqs.include.dir="<path-to-save-liboqs>\liboqs\build\include" -Dliboqs.lib.dir="<path-to-save-liboqs>\liboqs\build\lib"
```

### Linux/MacOS

#### Prerequisites
- JDK 1.8
- GCC
- CMake
- ninja-build
- Maven
- OpenSSL

#### Build Instructions

First, you must build the `main` branch of [liboqs](https://github.com/open-quantum-safe/liboqs/) according to the liboqs building instructions with static library, followed (optionally) by a `sudo cmake --install build` to ensure that the compiled library is visible system-wide (by default it installs under `/usr/local/include` and `/usr/local/lib` on Linux/macOS).

1. Build the liboqs C library to generate liboqs.a
```bash
cd <path-to-save-liboqs>
git clone https://github.com/open-quantum-safe/liboqs/
cmake -S . -B build
cmake --build build -j4
#optional 
sudo cmake --install build
cd ..
```

This step will generate the `liboqs/build/liboqs.a` file. 

### Building the Java OQS wrapper

To build the `liboqs-java` wrapper type for different operating systems add the `-P <OS>` flag, where `<OS> = {linux, macosx, windows}`.

For instance, to build `liboqs-java` for MacOS, type:
```
mvn package -P macosx -Dliboqs.include.dir="/usr/local/include" -Dliboqs.lib.dir="/usr/local/lib"
```
The above command will compile the C and Java files and also run the unit tests.

For those who doen't want the `liboqs` library to install system wide. You **have to** change `<liboqs.include.dir>` to `<path-to-save-liboqs>/liboqs/build/include` to and `<liboqs.lib.dir>` to `<path-to-save-liboqs>/liboqs/build/lib`
```
mvn package -P macosx -Dliboqs.include.dir="<path-to-save-liboqs>/liboqs/build/include" -Dliboqs.lib.dir="<path-to-save-liboqs>/liboqs/build/lib"
```

To build without running the default unit tests you can use the `-Dmaven.test.skip=true` command line option as follows:
```
mvn package -P macosx -Dliboqs.include.dir="/usr/local/include" -Dliboqs.lib.dir="/usr/local/lib" -Dmaven.test.skip=true
```

The default profile for building is `linux`, so when building on Linux the `-P <OS>` command line option may be omitted.

You may also omit the `-Dliboqs.include.dir` and `-Dliboqs.lib.dir` options in case you installed liboqs in `/usr/local` (true if you ran `sudo cmake --install build` after building liboqs).

Both the above commands will create a `target` directory with the build files, as well as a `src/main/resources` directory that will contain the `liboqs-jni.so` native library. Finally, a `liboqs-java.jar` will be created inside the `target` directory that will contain all the class files as well as the `liboqs-jni.so` native library.

### Building and running the examples

The examples include:

1. **Key Encapsulation example:**

    ![alt text][KEM-overview]

1. **Digital Signatures example:**

    ![alt text][DS-overview]

1. **Rand example:**
    Print random bytes from
    * OpenSSL
    * System (default)

#### 1) Key Encapsulation example

To compile and run the KEM example, type:

##### Windows

```
$ javac -cp target/liboqs-java.jar examples\KEMExample.java
$ java -cp "target\liboqs-java.jar;examples\" KEMExample
```

##### Linux/MacOS

```
javac -cp target/liboqs-java.jar examples/KEMExample.java
java -cp target/liboqs-java.jar:examples/ KEMExample
```

```
Supported KEMs:
BIKE-L1 BIKE-L3 BIKE-L5 Classic-McEliece-348864 Classic-McEliece-348864f Classic-McEliece-460896 Classic-McEliece-460896f Classic-McEliece-6688128 Classic-McEliece-6688128f Classic-McEliece-6960119 Classic-McEliece-6960119f Classic-McEliece-8192128 Classic-McEliece-8192128f HQC-128 HQC-192 HQC-256 Kyber512 Kyber768 Kyber1024 ML-KEM-512 ML-KEM-768 ML-KEM-1024 sntrup761 FrodoKEM-640-AES FrodoKEM-640-SHAKE FrodoKEM-976-AES FrodoKEM-976-SHAKE FrodoKEM-1344-AES FrodoKEM-1344-SHAKE 

Enabled KEMs:
BIKE-L1 BIKE-L3 BIKE-L5 Classic-McEliece-348864 Classic-McEliece-348864f Classic-McEliece-460896 Classic-McEliece-460896f Classic-McEliece-6688128 Classic-McEliece-6688128f Classic-McEliece-6960119 Classic-McEliece-6960119f Classic-McEliece-8192128 Classic-McEliece-8192128f HQC-128 HQC-192 HQC-256 Kyber512 Kyber768 Kyber1024 ML-KEM-512 ML-KEM-768 ML-KEM-1024 sntrup761 FrodoKEM-640-AES FrodoKEM-640-SHAKE FrodoKEM-976-AES FrodoKEM-976-SHAKE FrodoKEM-1344-AES FrodoKEM-1344-SHAKE 

KEM Details:
  Name: ML-KEM-512
  Version: FIPS203
  Claimed NIST level: 1
  Is IND-CCA: true
  Length public key (bytes): 800
  Length secret key (bytes): 1632
  Length ciphertext (bytes): 768
  Length shared secret (bytes): 32
  Length keypair seed (bytes): 64

Client public key:
A4 E7 5D DB AB 9D FA 13 ... 32 9C 08 3F 71 D6 BA 41 

It took 1 millisecs to generate the key pair.
It took 0 millisecs to encapsulate the secret.
It took 0 millisecs to decapsulate the secret.

Client shared secret:
5C BE 27 50 C8 7E 61 36 ... 07 60 EA 4C 3E 25 90 3F 

Server shared secret:
5C BE 27 50 C8 7E 61 36 ... 07 60 EA 4C 3E 25 90 3F 

Shared secrets coincide? true
```

#### 2) Signatures example

##### Windows

```
$ javac -cp target/liboqs-java.jar examples/SigExample.java
$ java -cp "target/liboqs-java.jar;examples\" SigExample
```

##### Linux/MacOS

```
javac -cp target/liboqs-java.jar examples/SigExample.java
java -cp target/liboqs-java.jar:examples/ SigExample
```

```
Supported signatures:
Dilithium2 Dilithium3 Dilithium5 ML-DSA-44 ML-DSA-65 ML-DSA-87 Falcon-512 Falcon-1024 Falcon-padded-512 Falcon-padded-1024 SPHINCS+-SHA2-128f-simple SPHINCS+-SHA2-128s-simple SPHINCS+-SHA2-192f-simple SPHINCS+-SHA2-192s-simple SPHINCS+-SHA2-256f-simple SPHINCS+-SHA2-256s-simple SPHINCS+-SHAKE-128f-simple SPHINCS+-SHAKE-128s-simple SPHINCS+-SHAKE-192f-simple SPHINCS+-SHAKE-192s-simple SPHINCS+-SHAKE-256f-simple SPHINCS+-SHAKE-256s-simple MAYO-1 MAYO-2 MAYO-3 MAYO-5 cross-rsdp-128-balanced cross-rsdp-128-fast cross-rsdp-128-small cross-rsdp-192-balanced cross-rsdp-192-fast cross-rsdp-192-small cross-rsdp-256-balanced cross-rsdp-256-fast cross-rsdp-256-small cross-rsdpg-128-balanced cross-rsdpg-128-fast cross-rsdpg-128-small cross-rsdpg-192-balanced cross-rsdpg-192-fast cross-rsdpg-192-small cross-rsdpg-256-balanced cross-rsdpg-256-fast cross-rsdpg-256-small OV-Is OV-Ip OV-III OV-V OV-Is-pkc OV-Ip-pkc OV-III-pkc OV-V-pkc OV-Is-pkc-skc OV-Ip-pkc-skc OV-III-pkc-skc OV-V-pkc-skc 

Enabled signatures:
Dilithium2 Dilithium3 Dilithium5 ML-DSA-44 ML-DSA-65 ML-DSA-87 Falcon-512 Falcon-1024 Falcon-padded-512 Falcon-padded-1024 SPHINCS+-SHA2-128f-simple SPHINCS+-SHA2-128s-simple SPHINCS+-SHA2-192f-simple SPHINCS+-SHA2-192s-simple SPHINCS+-SHA2-256f-simple SPHINCS+-SHA2-256s-simple SPHINCS+-SHAKE-128f-simple SPHINCS+-SHAKE-128s-simple SPHINCS+-SHAKE-192f-simple SPHINCS+-SHAKE-192s-simple SPHINCS+-SHAKE-256f-simple SPHINCS+-SHAKE-256s-simple MAYO-1 MAYO-2 MAYO-3 MAYO-5 cross-rsdp-128-balanced cross-rsdp-128-fast cross-rsdp-128-small cross-rsdp-192-balanced cross-rsdp-192-fast cross-rsdp-192-small cross-rsdp-256-balanced cross-rsdp-256-fast cross-rsdp-256-small cross-rsdpg-128-balanced cross-rsdpg-128-fast cross-rsdpg-128-small cross-rsdpg-192-balanced cross-rsdpg-192-fast cross-rsdpg-192-small cross-rsdpg-256-balanced cross-rsdpg-256-fast cross-rsdpg-256-small OV-Is OV-Ip OV-III OV-V OV-Is-pkc OV-Ip-pkc OV-III-pkc OV-V-pkc OV-Is-pkc-skc OV-Ip-pkc-skc OV-III-pkc-skc OV-V-pkc-skc 

Signature Details:
  Name: ML-DSA-44
  Version: FIPS204
  Claimed NIST level: 2
  Is IND-CCA: true
  Length public key (bytes): 1312
  Length secret key (bytes): 2560
  Maximum length signature (bytes): 2420

Signer public key:
2F F1 7A 8F FF EA 04 AA ... FD 51 A2 A0 80 5C 61 2B 

It took 1 millisecs to generate the key pair.
It took 0 millisecs to sign the message.
It took 0 millisecs to verify the signature.

Signature:
C0 41 9D 4D A9 B1 5F 4C ... 00 00 00 00 0A 20 2E 41 

Valid signature? true
```

#### 3) Rand example

##### Windows

```
$ javac -cp target/liboqs-java.jar examples\RandExample.java
$ java -cp "target/liboqs-java.jar;examples\" RandExample
```

##### Linux/MacOS
```
javac -cp target/liboqs-java.jar examples/RandExample.java
java -cp target/liboqs-java.jar:examples/ RandExample
```

```
OpenSSL:		19 0D 77 20 82 BA 59 69 38 32 3A 81 1B 50 6A A0 6F 81 14 35 06 14 9F 72 4F 6F D2 5F 68 E8 F7 40 
System (default):	81 2B 43 75 8A 22 63 21 28 D2 2D 1C 36 A1 19 19 22 AA E0 86 9A EE 6C A0 8C 52 E7 89 31 9C A3 6B 
```


## Troubleshooting
* __Compiler errors__

    * Cannot find `jni.h`:
        ```
        fatal error: jni.h: No such file or directory
            2 | #include <jni.h>
        compilation terminated.
        ```
        Try setting the `JAVA_HOME` environment variable.
        Then, try `ls $JAVA_HOME` to check whether the directory is empty or has contents. If it is empty, set `JAVA_HOME` to a correct JDK.

    * Cannot find `oqs/oqs.h`
        ```
        fatal error: oqs.h: No such file or directory
            5 | #include <oqs/oqs.h>
        compilation terminated.
        ```
        Try providing the `-Dliboqs.include.dir` and `-Dliboqs.lib.dir` command line options to maven as mentioned in the [build instructions](https://github.com/open-quantum-safe/liboqs-java#building-the-java-oqs-wrapper).

* __Runtime errors__
    * If Java cannot find native library:
        ```
          Exception in thread "main" java.lang.ExceptionInInitializerError
            at ...
          Caused by: java.lang.NullPointerException
            at org.openquantumsafe.Common.loadNativeLibrary(Common.java:51)
            at ...
        ```
        try passing to the java library path the directory that contains the native library (e.g., `java -Djava.library.path=src/main/resources/ -cp target/liboqs-java.jar:examples/ KEMExample`).
    
    * If Java cannot find `liboqs`:
        ```
        Exception in thread "main" java.lang.UnsatisfiedLinkError:
            ./liboqs-java/build/liboqs-jni.so: liboqs.so.0: cannot open shared object file: No such file or directory
        ```
        On Linux, try setting the `LD_LIBRARY_PATH` environment variable with the installation location of the `liboqs` shared library, i.e.,
        ```
        export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/usr/local/lib"
        ```
        On macOS, the corresponding environment variable is `DYLD_LIBRARY_PATH`.
        
    * If a hotspot "irrecoverable stack overflow" error is shown:
        ```
        An irrecoverable stack overflow has occurred.
        Please check if any of your loaded .so files has enabled executable stack (see man page execstack(8))
        #
        # A fatal error has been detected by the Java Runtime Environment:
        ```
        This could happen for algorithms that use a large stack size. Try adding the `-Xss` option to specify a different thread stack size. For example, `-Xss5m`.


## Team
The Open Quantum Safe project is led by [Douglas Stebila](https://www.douglas.stebila.ca/research/) and [Michele Mosca](http://faculty.iqc.uwaterloo.ca/mmosca/) at the University of Waterloo.

Contributors to the liboqs-java wrapper include:
* Dimitris Mouris ([@jimouris](https://github.com/jimouris)) (University of Delaware)
* Christian Paquin ([@christianpaquin](https://github.com/christianpaquin)) (Microsoft Research)

## License
`liboqs-java` is licensed under the MIT License; see [LICENSE](./LICENSE) for details.


[KEM-overview]: ./images/KEM.png
[DS-overview]: ./images/digital-signature.png
