deps:
	go get github.com/eclipse/paho.mqtt.golang

produce:
	go run mqtt-func.go

subscribe:
	mosquitto_sub -v -t "func/+/+"

