liboqs-java version 0.3.0
=========================

About
-----

The **Open Quantum Safe (OQS) project** has the goal of developing and prototyping quantum-resistant cryptography. More information on OQS can be found on our website: https://openquantumsafe.org/ and on Github at https://github.com/open-quantum-safe/.

**liboqs** is an open source C library for quantum-resistant cryptographic algorithms. See more about liboqs at [https://github.com/open-quantum-safe/liboqs/](https://github.com/open-quantum-safe/liboqs/), including a list of supported algorithms.

**liboqs-java** is an open source Java wrapper for the liboqs C library for quantum-resistant cryptographic algorithms. Details about liboqs-java can be found in [README.md](https://github.com/open-quantum-safe/liboqs-java/blob/master/README.md). See in particular limitations on intended use.

Release notes
=============

The initial release of liboqs-java was released on July 8, 2020. Its release page on GitHub is https://github.com/open-quantum-safe/liboqs-java/releases/tag/0.1.0.

Release 0.2.0 from January 2025 added support for Signature and Verify API's which accept a Context String.

Release 0.3.0 from April 2025 added a KEM API for deterministically generating a keypair from a seed. This function is currently supported only for the ML-KEM algorithm.
