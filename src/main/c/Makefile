OBJFILES := handle.o KEMs.o KeyEncapsulation.o Sigs.o Signature.o Rand.o
CFLAGS := -O3 -Wall

ifeq ($(OS),Windows_NT)
    uname_S := Windows
else
    uname_S := $(shell uname -s)
endif

ifeq ($(uname_S), Windows)
    target = liboqs-jni.dll
	# JAVA_INCLUDES := -I$(JAVA_HOME)\include -I$(JAVA_HOME)\include\win32
endif
ifeq ($(uname_S), Linux)
    target = liboqs-jni.so
	JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
endif
ifeq ($(uname_S), Darwin)
    target = liboqs-jni.jnilib
	JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/darwin
endif

$(target): $(OBJFILES)
	$(CC) -shared -o $@ $^ -loqs

handle.o: handle.c handle.h
	$(CC) -fPIC -c handle.c $(JAVA_INCLUDES) $(CFLAGS)

KEMs.o: KEMs.c KEMs.h
	$(CC) -fPIC -c KEMs.c $(JAVA_INCLUDES) $(CFLAGS)

KeyEncapsulation.o: KeyEncapsulation.c KeyEncapsulation.h
	$(CC) -fPIC -c KeyEncapsulation.c $(JAVA_INCLUDES) $(CFLAGS)

Sigs.o: Sigs.c Sigs.h
	$(CC) -fPIC -c Sigs.c $(JAVA_INCLUDES) $(CFLAGS)

Signature.o: Signature.c Signature.h
	$(CC) -fPIC -c Signature.c $(JAVA_INCLUDES) $(CFLAGS)

Rand.o: Rand.c Rand.h
	$(CC) -fPIC -c Rand.c $(JAVA_INCLUDES) $(CFLAGS)

clean:
	$(RM) *.o *.so *.class oqs/*.class *.log
