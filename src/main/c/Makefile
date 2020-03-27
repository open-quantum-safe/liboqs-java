ifeq ($(OS),Windows_NT)
    uname_S := Windows
else
    uname_S := $(shell uname -s)
endif

# Directories
BUILD_DIR := ../../../build
SOURCES_DIR := ./
SOURCES := $(wildcard $(SOURCES_DIR)/*.c)
OBJECTS := $(patsubst $(SOURCES_DIR)/%.c, $(BUILD_DIR)/%.o, $(SOURCES))

ifeq ($(uname_S), Windows)
    TARGET_LIB = $(BUILD_DIR)/liboqs-jni.dll
	# dir *.c /s
	# JAVA_INCLUDES := -I$(JAVA_HOME)\include -I$(JAVA_HOME)\include\win32
endif
ifeq ($(uname_S), Linux)
	CC := gcc
    TARGET_LIB := $(BUILD_DIR)/liboqs-jni.so
	JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
endif
ifeq ($(uname_S), Darwin)
	CC := clang
    TARGET_LIB := $(BUILD_DIR)/liboqs-jni.jnilib
	JAVA_INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/darwin
endif

# Compile flags
CFLAGS := -fPIC -Wall -O3 $(JAVA_INCLUDES)
LDFLAGS := -shared

all: dirs $(TARGET_LIB)

dirs:
	mkdir -p $(BUILD_DIR)

$(TARGET_LIB): $(OBJECTS)
	$(CC) ${LDFLAGS} $^ -o $@ -loqs

$(OBJECTS): $(BUILD_DIR)/%.o : $(SOURCES_DIR)/%.c
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -f $(BUILD_DIR)/*o $(TARGET_LIB)
