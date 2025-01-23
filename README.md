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

### Building the OQS dependency

#### Linux/MacOS
First, you must build the `main` branch of [liboqs](https://github.com/open-quantum-safe/liboqs/) according to the liboqs building instructions with shared library support enabled (add `-DBUILD_SHARED_LIBS=ON` to the `cmake` command), followed (optionally) by a `sudo ninja install` to ensure that the compiled library is visible system-wide (by default it installs under `/usr/local/include` and `/usr/local/lib` on Linux/macOS).

```
git clone -b main https://github.com/open-quantum-safe/liboqs.git
cd liboqs
mkdir build && cd build
cmake -GNinja -DBUILD_SHARED_LIBS=ON ..
ninja
sudo ninja install
```


### Building the Java OQS wrapper

To build the `liboqs-java` wrapper type for different operating systems add the `-P <OS>` flag, where `<OS> = {linux, macosx}`.

For instance, to build `liboqs-java` for MacOS, type:
```
mvn package -P macosx -Dliboqs.include.dir="/usr/local/include" -Dliboqs.lib.dir="/usr/local/lib"
```
The above command will compile the C and Java files and also run the unit tests.

To build without running the default unit tests you can use the `-Dmaven.test.skip=true` command line option as follows:
```
mvn package -P macosx -Dliboqs.include.dir="/usr/local/include" -Dliboqs.lib.dir="/usr/local/lib" -Dmaven.test.skip=true
```

The default profile for building is `linux`, so when building on Linux the `-P <OS>` command line option may be omitted.

You may also omit the `-Dliboqs.include.dir` and `-Dliboqs.lib.dir` options in case you installed liboqs in `/usr/local` (true if you ran `sudo ninja install` after building liboqs).

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

#### Linux/MacOS

##### 1) Key Encapsulation example

To compile and run the KEM example, type:
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

Client public key:
A8 37 25 CA 79 A5 55 42 ... AF 43 3A 54 6C 3C 34 30 

It took 1 millisecs to generate the key pair.
It took 0 millisecs to encapsulate the secret.
It took 0 millisecs to decapsulate the secret.

Client shared secret:
7D 3B BB C7 29 45 4B 2F ... 58 87 1D BB BD 35 9C 79 

Server shared secret:
7D 3B BB C7 29 45 4B 2F ... 58 87 1D BB BD 35 9C 79 

Shared secrets coincide? true
```

##### 2) Signatures example

```
javac -cp target/liboqs-java.jar examples/SigExample.java
java -cp target/liboqs-java.jar:examples/ SigExample
```

```
Supported signatures:
Dilithium2 Dilithium3 Dilithium5 ML-DSA-44 ML-DSA-65 ML-DSA-87 Falcon-512 Falcon-1024 Falcon-padded-512 Falcon-padded-1024 SPHINCS+-SHA2-128f-simple SPHINCS+-SHA2-128s-simple SPHINCS+-SHA2-192f-simple SPHINCS+-SHA2-192s-simple SPHINCS+-SHA2-256f-simple SPHINCS+-SHA2-256s-simple SPHINCS+-SHAKE-128f-simple SPHINCS+-SHAKE-128s-simple SPHINCS+-SHAKE-192f-simple SPHINCS+-SHAKE-192s-simple SPHINCS+-SHAKE-256f-simple SPHINCS+-SHAKE-256s-simple MAYO-1 MAYO-2 MAYO-3 MAYO-5 cross-rsdp-128-balanced cross-rsdp-128-fast cross-rsdp-128-small cross-rsdp-192-balanced cross-rsdp-192-fast cross-rsdp-192-small cross-rsdp-256-balanced cross-rsdp-256-fast cross-rsdp-256-small cross-rsdpg-128-balanced cross-rsdpg-128-fast cross-rsdpg-128-small cross-rsdpg-192-balanced cross-rsdpg-192-fast cross-rsdpg-192-small cross-rsdpg-256-balanced cross-rsdpg-256-fast cross-rsdpg-256-small 

Enabled signatures:
Dilithium2 Dilithium3 Dilithium5 ML-DSA-44 ML-DSA-65 ML-DSA-87 Falcon-512 Falcon-1024 Falcon-padded-512 Falcon-padded-1024 SPHINCS+-SHA2-128f-simple SPHINCS+-SHA2-128s-simple SPHINCS+-SHA2-192f-simple SPHINCS+-SHA2-192s-simple SPHINCS+-SHA2-256f-simple SPHINCS+-SHA2-256s-simple SPHINCS+-SHAKE-128f-simple SPHINCS+-SHAKE-128s-simple SPHINCS+-SHAKE-192f-simple SPHINCS+-SHAKE-192s-simple SPHINCS+-SHAKE-256f-simple SPHINCS+-SHAKE-256s-simple MAYO-1 MAYO-2 MAYO-3 MAYO-5 cross-rsdp-128-balanced cross-rsdp-128-fast cross-rsdp-128-small cross-rsdp-192-balanced cross-rsdp-192-fast cross-rsdp-192-small cross-rsdp-256-balanced cross-rsdp-256-fast cross-rsdp-256-small cross-rsdpg-128-balanced cross-rsdpg-128-fast cross-rsdpg-128-small cross-rsdpg-192-balanced cross-rsdpg-192-fast cross-rsdpg-192-small cross-rsdpg-256-balanced cross-rsdpg-256-fast cross-rsdpg-256-small 

Signature Details:
  Name: ML-DSA-44
  Version: FIPS204
  Claimed NIST level: 2
  Is IND-CCA: true
  Length public key (bytes): 1312
  Length secret key (bytes): 2560
  Maximum length signature (bytes): 2420

Signer public key:
CB CB 70 FF 1E B3 BA 26 ... A7 CF 7C 70 89 A1 1A 40 

It took 1 millisecs to generate the key pair.
It took 1 millisecs to sign the message.
It took 0 millisecs to verify the signature.

Signature:
ED 6F 67 B6 2E C9 31 FC ... 00 00 00 00 0F 21 2A 38 

Valid signature? true
```

##### 3) Rand example

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
