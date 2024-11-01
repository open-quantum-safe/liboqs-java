# liboqs-java windows 安装流程(详细版)

## 安装liboqs库

- 安装mingw-w64的GCC 11.5.0版本（这个版本应该无所谓）

[WinLibs - GCC+MinGW-w64 compiler for Windows](https://winlibs.com/#download-release)

这个下载zip之后，直接解压到需要安装的目录，中间不要有空格，然后加入环境变量。

控制面板->系统->系统信息->高级系统设置->高级->环境变量->PATH

PATH新增一行`E:\develop\mingw64\bin`

> 或者用命令行 setx PATH = E:\develop\mingw64\bin;%PATH% （不推荐）

- 安装cmake（我是通过winget命令行安装的，可以界面安装[https://cmake.org/download/]，要把cmake加入到PATH环境变量中）

```bash
winget install cmake
```

- 打开命令行看下是否安装成功

```bash
$ C:\Users\adije>gcc --version
gcc (MinGW-W64 x86_64-ucrt-posix-seh, built by Brecht Sanders) 11.5.0
Copyright (C) 2021 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

$ C:\Users\adije>cmake --version
cmake version 3.30.5

CMake suite maintained and supported by Kitware (kitware.com/cmake).
```

- 从git上面下载了liboqs-java的代码

```bash
git clone --recursive https://github.com/AdijeShen/liboqs-java
```

- 编译liboqs

```bash
cd liboqs
cmake -G "MinGW Makefiles" -DCMAKE_C_COMPILER=gcc -DBUILD_SHARED_LIBS=OFF -S . -B build
cmake --build build -j 4
cmake --install build
cd ..
```

## 使用liboqs-java

- 首先需要安装maven[https://maven.apache.org/](3.8.8)和jdk[https://www.openlogic.com/openjdk-downloads](1.8)，这个可以直接下载安装包安装，然后加入环境变量。

检查安装成功

```bash
$ java -version
openjdk version "1.8.0_432-432"
OpenJDK Runtime Environment (build 1.8.0_432-432-b06)
OpenJDK 64-Bit Server VM (build 25.432-b06, mixed mode)

$ mvn -version
Apache Maven 3.8.8 (4c87b05d9aedce574290d1acc98575ed5eb6cd39)
Maven home: E:\develop\apache-maven-3.8.8
Java version: 1.8.0_432-432, vendor: OpenLogic-OpenJDK, runtime: E:\develop\jdk8\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

然后下载liboqs-java

```bash
git clone --recursive https://github.com/AdijeShen/liboqs-java
```

