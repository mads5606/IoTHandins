This directory should contain all of the necessary Go dependencies.

How to run:

1. step: Compile

`javac -cp javasysmon-0.3.5.1.jar -d build src/com/iot/*.java`

2. step: Run each go file and the mosquitto sub command
`java -classpath build;javasysmon-0.3.5.1.jar com.iot.TestHarness siggen go run GoFiles/siggen/mqtt-siggen.go`

`java -classpath build;javasysmon-0.3.5.1.jar com.iot.TestHarness mavg go run GoFiles/mavg/mqtt-mavg.go`

`java -classpath build;javasysmon-0.3.5.1.jar com.iot.TestHarness func go run GoFiles/func/mqtt-func.go`

`java -classpath build;javasysmon-0.3.5.1.jar com.iot.TestHarness client go run mosquitto_sub -v -t "func/+/+"`
