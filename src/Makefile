JAVAC = javac
CFLAGS = -g
SRC_DIR = .
BIN_DIR = ../bin

SRCS = $(shell find $(SRC_DIR) -name '*.java')
CLASS = $(patsubst $(SRC_DIR)/%.java, $(BIN_DIR)/%.class, $(SRCS))

all: $(CLASS)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(CFLAGS) -d $(BIN_DIR) $<

clean:
	rm -rf $(BIN_DIR)

run: all
	java -cp $(BIN_DIR) Main

jdb: all
	jdb -sourcepath . -classpath ../bin Main

jdbf: all
	cp ../debug/jdb.ini .
	jdb -sourcepath . -classpath ../bin Main
	rm jdb.ini
