# liboqs-java: Java wrapper for [liboqs](https://github.com/open-quantum-safe/liboqs) [![License MIT][badge-license]](LICENSE)
## [Open Quantum Safe Project](https://openquantumsafe.org/)


**liboqs-java** offers a Java wrapper for the master branch of [Open Quantum Safe](https://openquantumsafe.org/) [liboqs](https://github.com/open-quantum-safe/liboqs/) C library, which is a C library for quantum-resistant cryptographic algorithms.

## Pre-requisites
liboqs-java depends on the [liboqs](https://github.com/open-quantum-safe/liboqs) C library; liboqs master branch must first be compiled as a Linux/macOS/Windows library, see the specific platform building instructions below.


## Building on POSIX (Linux/UNIX-like) platforms
First, you must build the master branch of liboqs according to the [liboqs building instructions](https://github.com/open-quantum-safe/liboqs#linuxmacos) with shared library support enabled (add `-DBUILD_SHARED_LIBS=ON` to the `cmake` command), followed (optionally) by a `sudo ninja install` to ensure that the compiled library is visible system-wide (by default it installs under `/usr/local/include` and `/usr/local/lib` on Linux/macOS).


### Compilation
```
$ make
```

### Examples
##### Key Encapsulation example

```
$ make run

Supported KEMs:
DEFAULT BIKE1-L1-CPA BIKE1-L3-CPA BIKE1-L1-FO BIKE1-L3-FO Classic-McEliece-348864 Classic-McEliece-348864f Classic-McEliece-460896 Classic-McEliece-460896f Classic-McEliece-6688128 Classic-McEliece-6688128f Classic-McEliece-6960119 Classic-McEliece-6960119f Classic-McEliece-8192128 Classic-McEliece-8192128f Kyber512 Kyber768 Kyber1024 Kyber512-90s Kyber768-90s Kyber1024-90s LEDAcryptKEM-LT12 LEDAcryptKEM-LT32 LEDAcryptKEM-LT52 NewHope-512-CCA NewHope-1024-CCA NTRU-HPS-2048-509 NTRU-HPS-2048-677 NTRU-HPS-4096-821 NTRU-HRSS-701 LightSaber-KEM Saber-KEM FireSaber-KEM BabyBear BabyBearEphem MamaBear MamaBearEphem PapaBear PapaBearEphem FrodoKEM-640-AES FrodoKEM-640-SHAKE FrodoKEM-976-AES FrodoKEM-976-SHAKE FrodoKEM-1344-AES FrodoKEM-1344-SHAKE SIDH-p434 SIDH-p503 SIDH-p610 SIDH-p751 SIDH-p434-compressed SIDH-p503-compressed SIDH-p610-compressed SIDH-p751-compressed SIKE-p434 SIKE-p503 SIKE-p610 SIKE-p751 SIKE-p434-compressed SIKE-p503-compressed SIKE-p610-compressed SIKE-p751-compressed 

Enabled KEMs:
DEFAULT BIKE1-L1-CPA BIKE1-L3-CPA BIKE1-L1-FO BIKE1-L3-FO Classic-McEliece-348864 Classic-McEliece-348864f Classic-McEliece-460896 Classic-McEliece-460896f Classic-McEliece-6688128 Classic-McEliece-6688128f Classic-McEliece-6960119 Classic-McEliece-6960119f Classic-McEliece-8192128 Classic-McEliece-8192128f Kyber512 Kyber768 Kyber1024 Kyber512-90s Kyber768-90s Kyber1024-90s LEDAcryptKEM-LT12 LEDAcryptKEM-LT32 LEDAcryptKEM-LT52 NewHope-512-CCA NewHope-1024-CCA NTRU-HPS-2048-509 NTRU-HPS-2048-677 NTRU-HPS-4096-821 NTRU-HRSS-701 LightSaber-KEM Saber-KEM FireSaber-KEM BabyBear BabyBearEphem MamaBear MamaBearEphem PapaBear PapaBearEphem FrodoKEM-640-AES FrodoKEM-640-SHAKE FrodoKEM-976-AES FrodoKEM-976-SHAKE FrodoKEM-1344-AES FrodoKEM-1344-SHAKE SIDH-p434 SIDH-p503 SIDH-p610 SIDH-p751 SIDH-p434-compressed SIDH-p503-compressed SIDH-p610-compressed SIDH-p751-compressed SIKE-p434 SIKE-p503 SIKE-p610 SIKE-p751 SIKE-p434-compressed SIKE-p503-compressed SIKE-p610-compressed SIKE-p751-compressed 

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



[badge-license]: https://img.shields.io/badge/license-MIT-green.svg?style=flat-square
