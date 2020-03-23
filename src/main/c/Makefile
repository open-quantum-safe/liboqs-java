LIBRARY := liboqs-jni.so
OBJFILES := handle.o KEMs.o KeyEncapsulation.o Sigs.o Signature.o Rand.o
JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

$(LIBRARY): $(OBJFILES)
	gcc -shared -o $@ $^ -loqs

handle.o: handle.c handle.h
	gcc -fPIC -c handle.c $(JAVA_INCLUDES)

KEMs.o: KEMs.c KEMs.h
	gcc -fPIC -c KEMs.c $(JAVA_INCLUDES)

KeyEncapsulation.o: KeyEncapsulation.c KeyEncapsulation.h
	gcc -fPIC -c KeyEncapsulation.c $(JAVA_INCLUDES)

Sigs.o: Sigs.c Sigs.h
	gcc -fPIC -c Sigs.c $(JAVA_INCLUDES)

Signature.o: Signature.c Signature.h
	gcc -fPIC -c Signature.c $(JAVA_INCLUDES)

Rand.o: Rand.c Rand.h
	gcc -fPIC -c Rand.c $(JAVA_INCLUDES)

clean:
	$(RM) *.o *.so *.class oqs/*.class *.log
