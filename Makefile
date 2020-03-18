LIBRARY := src/c/liboqs-jni.so

all: SimpleTest

SimpleTest: $(LIBRARY)
	javac SimpleTest.java

$(LIBRARY):
	$(MAKE) -C src/c

run:
	java -classpath . -Djava.library.path=./src/c/. SimpleTest

clean:
	$(MAKE) -C src/c clean
	$(MAKE) -C src/java/oqs clean
	$(RM) -f *.class
