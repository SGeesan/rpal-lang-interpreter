JFLAGS = -g
JC = javac
SRC_DIR = .
.PHONY: test

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

test:
	@java myrpal test/rpal_01 > test/output.01
	@diff -q test/output.01 test/rpal_01_out > /dev/null && \
	echo "Test 1 (rpal_01): PASSED" || echo "Test 1 (rpal_01): FAILED"

	@java myrpal test/add > test/output.02
	@diff -q test/output.02 test/add_out > /dev/null && \
	echo "Test 2 (add): PASSED" || echo "Test 2 (add): FAILED"

	@java myrpal test/conc.1 > test/output.03
	@diff -q test/output.03 test/conc.1_out > /dev/null && \
	echo "Test 3 (conc.1): PASSED" || echo "Test 3 (conc.1): FAILED"

	@java myrpal test/defns.1 > test/output.04
	@diff -q test/output.04 test/defns.1_out > /dev/null && \
	echo "Test 4 (defns.1): PASSED" || echo "Test 4 (defns.1): FAILED"

	@java myrpal test/fn1 > test/output.05
	@diff -q test/output.05 test/fn1_out > /dev/null && \
	echo "Test 5 (fn1): PASSED" || echo "Test 5 (fn1): FAILED"

	@java myrpal test/fn2 > test/output.06
	@diff -q test/output.06 test/fn2_out > /dev/null && \
	echo "Test 6 (fn2): PASSED" || echo "Test 6 (fn2): FAILED"

	@java myrpal test/fn3 > test/output.07
	@diff -q test/output.07 test/fn3_out > /dev/null && \
	echo "Test 7 (fn3): PASSED" || echo "Test 7 (fn3): FAILED"

	@java myrpal test/ftst > test/output.08
	@diff -q test/output.08 test/ftst_out > /dev/null && \
	echo "Test 8 (ftst): PASSED" || echo "Test 8 (ftst): FAILED"

	@java myrpal test/infix > test/output.09
	@diff -q test/output.09 test/infix_out > /dev/null && \
	echo "Test 9 (infix): PASSED" || echo "Test 9 (infix): FAILED"

	@java myrpal test/infix2 > test/output.10
	@diff -q test/output.10 test/infix2_out > /dev/null && \
	echo "Test 10 (infix2): PASSED" || echo "Test 10 (infix2): FAILED"

	@java myrpal test/pairs1 > test/output.11
	@diff -q test/output.11 test/pairs1_out > /dev/null && \
	echo "Test 11 (pairs1): PASSED" || echo "Test 11 (pairs1): FAILED"

	@java myrpal test/pairs2 > test/output.12
	@diff -q test/output.12 test/pairs2_out > /dev/null && \
	echo "Test 12 (pairs2): PASSED" || echo "Test 12 (pairs2): FAILED"

	@java myrpal test/pairs3 > test/output.13
	@diff -q test/output.13 test/pairs3_out > /dev/null && \
	echo "Test 13 (pairs3): PASSED" || echo "Test 13 (pairs3): FAILED"

	@java myrpal test/picture > test/output.14
	@diff -q test/output.14 test/picture_out > /dev/null && \
	echo "Test 14 (picture): PASSED" || echo "Test 14 (picture): FAILED"

	@java myrpal test/towers > test/output.15
	@diff -q test/output.15 test/towers_out > /dev/null && \
	echo "Test 15 (towers): PASSED" || echo "Test 15 (towers): FAILED"

	@java myrpal test/vectorsum > test/output.16
	@diff -q test/output.16 test/vectorsum_out > /dev/null && \
	echo "Test 16 (vectorsum): PASSED" || echo "Test 16 (vectorsum): FAILED"

	@echo "All tests Passed!"
	
clean:
	$(RM) $(JAVA_FILES:.java=.class)
	$(RM) test/output.*