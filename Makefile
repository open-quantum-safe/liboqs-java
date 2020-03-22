LIBRARY := src/main/c/liboqs-jni.so

all: KEM_example Sig_example

KEM_example: $(LIBRARY)
	javac -classpath src/main/java/ src/test/java/org/openquantumsafe/KEM_Test.java

Sig_example: $(LIBRARY)
	javac -classpath src/main/java/ src/test/java/org/openquantumsafe/Sig_Test.java

$(LIBRARY):
	$(MAKE) -C src/main/c

run-kem:
	java -classpath src/main/java/:src/test/java/ -Djava.library.path=./src/main/c/ org.openquantumsafe.KEM_Test

run-sig:
	java -classpath src/main/java/:src/test/java/ -Djava.library.path=./src/main/c/ org.openquantumsafe.Sig_Test

clean-c:
	$(MAKE) -C src/main/c clean

clean-java:
	$(MAKE) -C src/main/java/org/openquantumsafe clean
	
clean:
	$(MAKE) -C src/main/c clean
	$(MAKE) -C src/main/java/org/openquantumsafe clean
	$(MAKE) -C src/test/java/org/openquantumsafe clean
