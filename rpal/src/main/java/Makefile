JFLAGS = -g
JC = javac
SRC_DIR = .
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

JAVA_FILES = $(wildcard $(SRC_DIR)/com/rpal/lex/*.java) \
$(wildcard $(SRC_DIR)/com/rpal/parser/*.java) \
$(wildcard $(SRC_DIR)/com/rpal/cse/*.java) \
$(wildcard $(SRC_DIR)/*.java) 


default: classes

classes: $(JAVA_FILES:.java=.class)

run:
	java -cp $(OUT_DIR) myrpal

clean:
	$(RM) $(JAVA_FILES:.java=.class)

