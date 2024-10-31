代码由https://github.com/open-quantum-safe/liboqs-java，修改得到

这是本地构建用的代码，需要在linux环境运行，需求jdk1.8, gcc, cmake, ninja-build, maven, openssl

1. (已经编译完成，可跳过) 编译liboqs的C代码库
```
cd liboqs
mkdir build && cd build
cmake -GNinja ..
ninja
```

2. 编译liboqs-java
```
mvn package -P linux
```

3. 安装至maven本地仓库
```
mvn install
```

4. 可以运行KEM示例:
```
$ javac -cp target/liboqs-java.jar examples/KEMExample.java
$ java -cp target/liboqs-java.jar:examples/ KEMExample
```
