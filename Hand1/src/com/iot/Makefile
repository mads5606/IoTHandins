TARGETS = \
	Logger.class \
	OutputLogger.class \
	ProcessLogger.class \
	TestHarness.class \

CP = -classpath ".:javasysmon-0.3.5.1.jar"

all: ${TARGETS}

clean:
	touch ${TARGETS}
	rm    ${TARGETS}

mrproper: clean
	touch d~
	rm    *~

deps:
	echo Download from https://jar-download.com/artifact-search/javasysmon and extract jar file here. Code expects the result to be named "javasysmon-0.3.5.1.jar".


Logger.class: Logger.java
	javac Logger.java

OutputLogger.class: OutputLogger.java
	javac OutputLogger.java

ProcessLogger.class: ProcessLogger.java
	javac ${CP} ProcessLogger.java

TestHarness.class: TestHarness.java
	javac ${CP} TestHarness.java

TestHarness: ${TARGETS}
	java ${CP} TestHarness run42 chromium-browser

