package main

import (
    "fmt"
    "encoding/json"
    "sync"
    
    "github.com/eclipse/paho.mqtt.golang"
)

type Sample struct {
    Time  float64 `json:"time"`
    Value float64 `json:"value"`
}

var (
    brokers []string = []string{"tcp://127.0.0.1:1883"}
    pattern   string = "siggen/+/+"
    dispatch map[string]chan Sample = make(map[string]chan Sample)
    dispatch_mux sync.Mutex
    client mqtt.Client
)

const (
    WINDOW_SIZE int = 3
)

func mavg (topic string, channel chan Sample) {
    otopic := "mavg"+topic[6:]
    fmt.Println("Republishing moving average to", otopic)
    
    // initialize window and sum
    window := make([]float64, WINDOW_SIZE)
    for i := range window {
        window[i] = 0.0
    }
    sum := 0.0
    
    // service loop
    i := 0
    for sample := range channel {
        value := sample.Value
        //fmt.Println(otopic, value)
        
        // update window and sum
        sum += value - window[i%WINDOW_SIZE]
        window[i%WINDOW_SIZE] = value
        
        // build message
        var new_sample Sample = Sample{sample.Time, sum}
        message, _ := json.Marshal(new_sample)

        //fmt.Println(message)
        fmt.Println(fmt.Sprintf("e%.0f-%.2f", sample.Time, sample.Value))
        // publish
        client.Publish(otopic, 1, false, message)
        
        i++
    }
}

func dispatch_sample (client mqtt.Client, message mqtt.Message) {
    var topic string = message.Topic()
    var sample Sample
    
    // unmarshal
    err := json.Unmarshal(message.Payload(), &sample)
    if err!=nil {
        fmt.Println("Unable to unmarshal incoming sample:", err)
        return
    }
    //fmt.Println(sample.Time, ", ", sample.Value)
    fmt.Println(fmt.Sprintf("s%.0f-%.2f", sample.Time, sample.Value))
    
    // make sure that channel exists
    dispatch_mux.Lock()
    channel, ok := dispatch[topic]
    if !ok {
        channel = make(chan Sample, 2)
        go mavg(topic, channel);
        dispatch[topic] = channel
    }
    dispatch_mux.Unlock()
    
    // queue channel
    channel <- sample
}

func mqtt_subscribe () {
    // configure options
    options := mqtt.NewClientOptions()
    for _, broker := range brokers {
      options.AddBroker(broker)
    }
    
    // start mqtt client
    client = mqtt.NewClient(options)
    if token := client.Connect(); token.Wait() && token.Error() != nil {
        panic(token.Error())
    }
    
    // set up subscription
    if token := client.Subscribe(pattern, 2, dispatch_sample); token.Wait() && token.Error() != nil {
        panic(token.Error())
    }
}

func main () {
    mqtt_subscribe()
    
    select{} // block forever
}
