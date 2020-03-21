# liboqs-java: Java wrapper for [liboqs](https://github.com/open-quantum-safe/liboqs) [![License MIT][badge-license]](LICENSE)
## [Open Quantum Safe Project](https://openquantumsafe.org/)


**liboqs-java** offers a Java wrapper for the master branch of [Open Quantum Safe](https://openquantumsafe.org/) [liboqs](https://github.com/open-quantum-safe/liboqs/) C library, which is a C library for quantum-resistant cryptographic algorithms.

## Pre-requisites
liboqs-java depends on the [liboqs](https://github.com/open-quantum-safe/liboqs) C library; liboqs master branch must first be compiled as a Linux/macOS/Windows library, see the specific platform building instructions below.



## Building on POSIX (Linux/UNIX-like) platforms
First, you must build the master branch of liboqs according to the [liboqs building instructions](https://github.com/open-quantum-safe/liboqs#linuxmacos) with shared library support enabled (add `-DBUILD_SHARED_LIBS=ON` to the `cmake` command), followed (optionally) by a `sudo ninja install` to ensure that the compiled library is visible system-wide (by default it installs under `/usr/local/include` and `/usr/local/lib` on Linux/macOS).


#### KEM_Test
The file [KEM_Test.java](./src/test/java/org/openquantumsafe/KEM_Test.java) invokes the `max_number_KEMs` method which is a Java wrapper for `OQS_KEM_alg_count`.

```
$ make
$ make run
```



[badge-license]: https://img.shields.io/badge/license-MIT-green.svg?style=flat-square
