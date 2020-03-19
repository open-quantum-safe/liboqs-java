LIBRARY := liboqs-jni.so
OBJFILES := org_openquantumsafe_KEMs.o org_openquantumsafe_KeyEncapsulation.o
JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

$(LIBRARY): $(OBJFILES)
	gcc -shared -o $@ $^ -loqs 

org_openquantumsafe_KEMs.o: org_openquantumsafe_KEMs.c org_openquantumsafe_KEMs.h
	gcc -fPIC -c org_openquantumsafe_KEMs.c $(JAVA_INCLUDES)
	
org_openquantumsafe_KeyEncapsulation.o: org_openquantumsafe_KeyEncapsulation.c org_openquantumsafe_KeyEncapsulation.h
	gcc -fPIC -c org_openquantumsafe_KeyEncapsulation.c $(JAVA_INCLUDES)

clean:
	$(RM) *.o *.so *.class oqs/*.class *.log
