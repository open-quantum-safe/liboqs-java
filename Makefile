LIBRARY := src/main/c/liboqs-jni.so

all: KEM_Test

KEM_Test: $(LIBRARY)
	javac -classpath src/main/java/ src/test/java/org/openquantumsafe/KEM_Test.java

$(LIBRARY):
	$(MAKE) -C src/main/c

run:
	java -classpath src/main/java/:src/test/java/ -Djava.library.path=./src/main/c/ org.openquantumsafe.KEM_Test

clean:
	$(MAKE) -C src/main/c clean
	$(MAKE) -C src/main/java/org/openquantumsafe clean
	$(MAKE) -C src/test/java/org/openquantumsafe clean
