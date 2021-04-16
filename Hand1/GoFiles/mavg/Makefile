deps:
	go get github.com/eclipse/paho.mqtt.golang

produce:
	go run mqtt-mavg.go

subscribe:
	mosquitto_sub -v -t "mavg/+/+"

