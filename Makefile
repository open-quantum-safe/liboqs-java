BUILD_DIR := ./build

all: liboqs-jni KEM_example Sig_example Rand_example

tests: KEMTest SigTest

############################ Compile JNI code ############################

liboqs-jni:
	$(MAKE) -C src/main/c


############################ Compile examples ############################

KEM_example: liboqs-jni
	javac -cp src/main/java/ src/examples/java/org/openquantumsafe/KEMExample.java -d $(BUILD_DIR)

Sig_example: liboqs-jni
	javac -cp src/main/java/ src/examples/java/org/openquantumsafe/SigExample.java -d $(BUILD_DIR)

Rand_example: liboqs-jni
	javac -cp src/main/java/ src/examples/java/org/openquantumsafe/RandExample.java -d $(BUILD_DIR)


############################ Compile Tests ############################

KEMTest: liboqs-jni
	javac -cp lib/junit-4.13.jar:src/main/java src/test/java/org/openquantumsafe/KEMTest.java -d $(BUILD_DIR)

SigTest: liboqs-jni
	javac -cp lib/junit-4.13.jar:src/main/java src/test/java/org/openquantumsafe/SigTest.java -d $(BUILD_DIR)


############################ Run examples ############################

run-kem:
	java -Xss10M -cp $(BUILD_DIR) -Djava.library.path=$(BUILD_DIR) org.openquantumsafe.KEMExample

run-sig:
	java -Xss10M -cp $(BUILD_DIR) -Djava.library.path=$(BUILD_DIR) org.openquantumsafe.SigExample

run-rand:
	java -Xss10M -cp $(BUILD_DIR) -Djava.library.path=$(BUILD_DIR) org.openquantumsafe.RandExample


############################ Run Tests ############################

run-tests: tests
	$(MAKE) run-kem-test
	$(MAKE) run-sig-test

run-kem-test:
	java -Xss10M -cp lib/junit-4.13.jar:lib/hamcrest-2.2.jar:$(BUILD_DIR) -Djava.library.path=$(BUILD_DIR) org.junit.runner.JUnitCore org.openquantumsafe.KEMTest

run-sig-test:
	java -Xss10M -cp lib/junit-4.13.jar:lib/hamcrest-2.2.jar:$(BUILD_DIR) -Djava.library.path=$(BUILD_DIR) org.junit.runner.JUnitCore org.openquantumsafe.SigTest


############################ Clean ############################

clean-c:
	$(MAKE) -C src/main/c clean

clean-java:
	$(RM) -r $(BUILD_DIR)/org/openquantumsafe/*.class

clean:
	$(RM) -r $(BUILD_DIR)
	$(RM) *.log
