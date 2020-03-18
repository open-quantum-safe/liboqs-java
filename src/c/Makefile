LIBRARY := liboqs-jni.so
OBJFILES := src_java_oqs_KEMs.o src_java_oqs_KeyEncapsulation.o
JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

$(LIBRARY): $(OBJFILES)
	gcc -shared -o $@ $^ -loqs 

src_java_oqs_KEMs.o: src_java_oqs_KEMs.c src_java_oqs_KEMs.h
	gcc -fPIC -c src_java_oqs_KEMs.c $(JAVA_INCLUDES)
	
src_java_oqs_KeyEncapsulation.o: src_java_oqs_KeyEncapsulation.c src_java_oqs_KeyEncapsulation.h
	gcc -fPIC -c src_java_oqs_KeyEncapsulation.c $(JAVA_INCLUDES)

clean:
	$(RM) *.o *.so *.class oqs/*.class *.log
