LIBRARY := liboqs-jni.so
OBJFILES := KEMs.o KeyEncapsulation.o Sigs.o
JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

$(LIBRARY): $(OBJFILES)
	gcc -shared -o $@ $^ -loqs 

KEMs.o: KEMs.c KEMs.h
	gcc -fPIC -c KEMs.c $(JAVA_INCLUDES)
	
KeyEncapsulation.o: KeyEncapsulation.c KeyEncapsulation.h
	gcc -fPIC -c KeyEncapsulation.c $(JAVA_INCLUDES)

Sigs.o: Sigs.c Sigs.h
	gcc -fPIC -c Sigs.c $(JAVA_INCLUDES)
		
clean:
	$(RM) *.o *.so *.class oqs/*.class *.log
