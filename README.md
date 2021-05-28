[![CircleCI Build Status](https://circleci.com/gh/open-quantum-safe/liboqs-java/tree/master.svg?style=svg)](https://circleci.com/gh/open-quantum-safe/liboqs-java/tree/master)

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
Builds have been tested on Linux (Ubuntu 18.04 LTS, 19.10, and 20.04) and macOS Mojave with OpenJDK 8, 9, 11.

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
$ mvn package -P macosx -Dliboqs.include.dir="/usr/local/include" -Dliboqs.lib.dir="/usr/local/lib"
```
The above command will compile the C and Java files and also run the unit tests.

To build without running the default unit tests you can use the `-Dmaven.test.skip=true` command line option as follows:
```
$ mvn package -P macosx -Dliboqs.include.dir="/usr/local/include" -Dliboqs.lib.dir="/usr/local/lib" -Dmaven.test.skip=true
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
    * NIST-KAT
    * OpenSSL
    * System (default)

#### Linux/MacOS

##### 1) Key Encapsulation example

To compile and run the KEM example, type:
```
$ javac -cp target/liboqs-java.jar examples/KEMExample.java
$ java -cp target/liboqs-java.jar:examples/ KEMExample
```

```
Supported KEMs:
  DEFAULT BIKE1-L1-CPA BIKE1-L3-CPA BIKE1-L1-FO BIKE1-L3-FO Classic-McEliece-348864 Classic-McEliece-348864f
  Classic-McEliece-460896 Classic-McEliece-460896f Classic-McEliece-6688128 Classic-McEliece-6688128f
  Classic-McEliece-6960119 Classic-McEliece-6960119f Classic-McEliece-8192128 Classic-McEliece-8192128f
  Kyber512 Kyber768 Kyber1024 Kyber512-90s Kyber768-90s Kyber1024-90s LEDAcryptKEM-LT12 LEDAcryptKEM-LT32
  LEDAcryptKEM-LT52 NewHope-512-CCA NewHope-1024-CCA NTRU-HPS-2048-509 NTRU-HPS-2048-677 NTRU-HPS-4096-821
  NTRU-HRSS-701 LightSaber-KEM Saber-KEM FireSaber-KEM BabyBear BabyBearEphem MamaBear MamaBearEphem PapaBear
  PapaBearEphem FrodoKEM-640-AES FrodoKEM-640-SHAKE FrodoKEM-976-AES FrodoKEM-976-SHAKE FrodoKEM-1344-AES
  FrodoKEM-1344-SHAKE SIDH-p434 SIDH-p503 SIDH-p610 SIDH-p751 SIDH-p434-compressed SIDH-p503-compressed
  SIDH-p610-compressed SIDH-p751-compressed SIKE-p434 SIKE-p503 SIKE-p610 SIKE-p751 SIKE-p434-compressed
  SIKE-p503-compressed SIKE-p610-compressed SIKE-p751-compressed

Enabled KEMs:
  DEFAULT BIKE1-L1-CPA BIKE1-L3-CPA BIKE1-L1-FO BIKE1-L3-FO Classic-McEliece-348864 Classic-McEliece-348864f
  Classic-McEliece-460896 Classic-McEliece-460896f Classic-McEliece-6688128 Classic-McEliece-6688128f
  Classic-McEliece-6960119 Classic-McEliece-6960119f Classic-McEliece-8192128 Classic-McEliece-8192128f
  Kyber512 Kyber768 Kyber1024 Kyber512-90s Kyber768-90s Kyber1024-90s LEDAcryptKEM-LT12 LEDAcryptKEM-LT32
  LEDAcryptKEM-LT52 NewHope-512-CCA NewHope-1024-CCA NTRU-HPS-2048-509 NTRU-HPS-2048-677 NTRU-HPS-4096-821
  NTRU-HRSS-701 LightSaber-KEM Saber-KEM FireSaber-KEM BabyBear BabyBearEphem MamaBear MamaBearEphem PapaBear
  PapaBearEphem FrodoKEM-640-AES FrodoKEM-640-SHAKE FrodoKEM-976-AES FrodoKEM-976-SHAKE FrodoKEM-1344-AES
  FrodoKEM-1344-SHAKE SIDH-p434 SIDH-p503 SIDH-p610 SIDH-p751 SIDH-p434-compressed SIDH-p503-compressed
  SIDH-p610-compressed SIDH-p751-compressed SIKE-p434 SIKE-p503 SIKE-p610 SIKE-p751 SIKE-p434-compressed
  SIKE-p503-compressed SIKE-p610-compressed SIKE-p751-compressed

KEM Details:
  Name: FrodoKEM-640-AES
  Version: https://github.com/Microsoft/PQCrypto-LWEKE/commit/d5bbd0417ba111b08a959c0042a1dcc65fb14a89
  Claimed NIST level: 1
  Is IND-CCA: true
  Length public key (bytes): 9616
  Length secret key (bytes): 19888
  Length ciphertext (bytes): 9720
  Length shared secret (bytes): 16

Client public key:
14 E7 26 B9 28 F7 F8 23 ... 16 C3 D0 62 1C B3 D1 EB

It took 1 millisecs to generate the key pair.
It took 2 millisecs to encapsulate the secret.
It took 1 millisecs to decapsulate the secret.

Client shared secret:
B0 8A C0 62 69 68 44 D3 52 6A F9 9E 5A 42 26 16

Server shared secret:
B0 8A C0 62 69 68 44 D3 52 6A F9 9E 5A 42 26 16

Shared secrets coincide? true
```

##### 2) Signatures example

```
$ javac -cp target/liboqs-java.jar examples/SigExample.java
$ java -cp target/liboqs-java.jar:examples/ SigExample
```

```
Supported signatures:
  DEFAULT DILITHIUM_2 DILITHIUM_3 DILITHIUM_4 Falcon-512 Falcon-1024 MQDSS-31-48 MQDSS-31-64 Rainbow-Ia-Classic
  Rainbow-Ia-Cyclic Rainbow-Ia-Cyclic-Compressed Rainbow-IIIc-Classic Rainbow-IIIc-Cyclic
  Rainbow-IIIc-Cyclic-Compressed Rainbow-Vc-Classic Rainbow-Vc-Cyclic Rainbow-Vc-Cyclic-Compressed
  SPHINCS+-Haraka-128f-robust SPHINCS+-Haraka-128f-simple SPHINCS+-Haraka-128s-robust SPHINCS+-Haraka-128s-simple
  SPHINCS+-Haraka-192f-robust SPHINCS+-Haraka-192f-simple SPHINCS+-Haraka-192s-robust SPHINCS+-Haraka-192s-simple
  SPHINCS+-Haraka-256f-robust SPHINCS+-Haraka-256f-simple SPHINCS+-Haraka-256s-robust SPHINCS+-Haraka-256s-simple
  SPHINCS+-SHA256-128f-robust SPHINCS+-SHA256-128f-simple SPHINCS+-SHA256-128s-robust SPHINCS+-SHA256-128s-simple
  SPHINCS+-SHA256-192f-robust SPHINCS+-SHA256-192f-simple SPHINCS+-SHA256-192s-robust SPHINCS+-SHA256-192s-simple
  SPHINCS+-SHA256-256f-robust SPHINCS+-SHA256-256f-simple SPHINCS+-SHA256-256s-robust SPHINCS+-SHA256-256s-simple
  SPHINCS+-SHAKE256-128f-robust SPHINCS+-SHAKE256-128f-simple SPHINCS+-SHAKE256-128s-robust
  SPHINCS+-SHAKE256-128s-simple SPHINCS+-SHAKE256-192f-robust SPHINCS+-SHAKE256-192f-simple
  SPHINCS+-SHAKE256-192s-robust SPHINCS+-SHAKE256-192s-simple SPHINCS+-SHAKE256-256f-robust
  SPHINCS+-SHAKE256-256f-simple SPHINCS+-SHAKE256-256s-robust SPHINCS+-SHAKE256-256s-simple picnic_L1_FS
  picnic_L1_UR picnic_L3_FS picnic_L3_UR picnic_L5_FS picnic_L5_UR picnic2_L1_FS picnic2_L3_FS picnic2_L5_FS
  qTesla-p-I qTesla-p-III

Enabled signatures:
  DEFAULT DILITHIUM_2 DILITHIUM_3 DILITHIUM_4 Falcon-512 Falcon-1024 MQDSS-31-48 MQDSS-31-64 Rainbow-Ia-Classic
  Rainbow-Ia-Cyclic Rainbow-Ia-Cyclic-Compressed Rainbow-IIIc-Classic Rainbow-IIIc-Cyclic
  Rainbow-IIIc-Cyclic-Compressed Rainbow-Vc-Classic Rainbow-Vc-Cyclic Rainbow-Vc-Cyclic-Compressed
  SPHINCS+-Haraka-128f-robust SPHINCS+-Haraka-128f-simple SPHINCS+-Haraka-128s-robust SPHINCS+-Haraka-128s-simple
  SPHINCS+-Haraka-192f-robust SPHINCS+-Haraka-192f-simple SPHINCS+-Haraka-192s-robust SPHINCS+-Haraka-192s-simple
  SPHINCS+-Haraka-256f-robust SPHINCS+-Haraka-256f-simple SPHINCS+-Haraka-256s-robust SPHINCS+-Haraka-256s-simple
  SPHINCS+-SHA256-128f-robust SPHINCS+-SHA256-128f-simple SPHINCS+-SHA256-128s-robust SPHINCS+-SHA256-128s-simple
  SPHINCS+-SHA256-192f-robust SPHINCS+-SHA256-192f-simple SPHINCS+-SHA256-192s-robust SPHINCS+-SHA256-192s-simple
  SPHINCS+-SHA256-256f-robust SPHINCS+-SHA256-256f-simple SPHINCS+-SHA256-256s-robust SPHINCS+-SHA256-256s-simple
  SPHINCS+-SHAKE256-128f-robust SPHINCS+-SHAKE256-128f-simple SPHINCS+-SHAKE256-128s-robust
  SPHINCS+-SHAKE256-128s-simple SPHINCS+-SHAKE256-192f-robust SPHINCS+-SHAKE256-192f-simple
  SPHINCS+-SHAKE256-192s-robust SPHINCS+-SHAKE256-192s-simple SPHINCS+-SHAKE256-256f-robust
  SPHINCS+-SHAKE256-256f-simple SPHINCS+-SHAKE256-256s-robust SPHINCS+-SHAKE256-256s-simple picnic_L1_FS
  picnic_L1_UR picnic_L3_FS picnic_L3_UR picnic_L5_FS picnic_L5_UR picnic2_L1_FS picnic2_L3_FS picnic2_L5_FS
  qTesla-p-I qTesla-p-III

Signature Details:
  Name: DILITHIUM_2
  Version: https://github.com/pq-crystals/dilithium/commit/c1b40fd599e71f65aa18be64dd6c3fc8e84b0c08
  Claimed NIST level: 1
  Is IND-CCA: true
  Length public key (bytes): 1184
  Length secret key (bytes): 2800
  Maximum length signature (bytes): 2044

Signer public key:
0C 3A 73 48 09 14 18 8D ... 70 0B 3C 13 CF 43 F2 AD

It took 0 millisecs to generate the key pair.
It took 2 millisecs to sign the message.
It took 1 millisecs to verify.

Signature:
6D 06 5E C0 32 DB 70 A8 ... AF C9 F0 28 12 43 6C 04

Valid signature? true
```

##### 3) Rand example

```
$ javac -cp target/liboqs-java.jar examples/RandExample.java
$ java -cp target/liboqs-java.jar:examples/ RandExample
```

```
NIST-KAT:           BF E7 5C 34 F9 1C 54 44 30 CD B1 61 5B FF 3D 92 31 17 38 BD 71 61 0C 22 CD F7 B8 23 D9 7C 27 F3
OpenSSL:            86 B6 46 9C 56 44 6B FB F8 B1 37 F0 86 4D 4D 74 0F FD 51 99 82 D6 89 02 40 B9 45 CF F9 3A 4D 70
System (default):   37 55 6F 4F 03 53 BB 71 E8 70 C2 3D DF 85 69 57 30 CE FA 11 EF 50 8A F5 AE 25 35 6F 91 CF EC 1D
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
        try setting the `LD_LIBRARY_PATH` environment variable with the installation location of the `liboqs` shared library, i.e.,
        ```
        export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/usr/local/lib"
        ```
        
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

[badge-license]: https://img.shields.io/badge/license-MIT-brightgreen.svg?style=svg
[badge-circleci]: https://img.shields.io/circleci/build/github/open-quantum-safe/liboqs-java?logo=circleci
