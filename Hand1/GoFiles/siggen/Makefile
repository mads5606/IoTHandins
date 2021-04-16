deps:
	go get github.com/eclipse/paho.mqtt.golang

produce:
	go run mqtt-siggen.go

subscribe:
	mosquitto_sub -v -t "siggen/+/+"

