代码由https://github.com/open-quantum-safe/liboqs-java，修改得到，相比起原本的`liboqs-java`库，这个库里面使用了`liboqs.a`，而不是`liboqs.so`，所以不需要将`liboqs`安装到系统目录，只需要从`./liboqs/`文件夹编译得到`liboqs.a`即可。

这是本地构建用的代码，需要在linux环境运行，需求jdk1.8, gcc, cmake, ninja-build, maven, openssl

## linux下构建指南
1. 下载代码
```bash
git clone --recursive https://github.com/AdijeShen/liboqs-java
```

2. 编译liboqs的C代码库得到liboqs.a
```bash
cd liboqs
cmake -S . -B build
cmake --build build -j4
cd ..
```
这一步会生成`liboqs/build/liboqs.a`文件

3. 编译liboqs-java
```bash
mvn package
```

这一步会生成`target/liboqs-java.jar`和`target/classes/liboqs-jni.so`文件

4. 可以运行KEM示例:
```bash
javac -cp target/liboqs-java.jar examples/KEMExample.java
java -cp target/liboqs-java.jar:examples/ KEMExample
```
