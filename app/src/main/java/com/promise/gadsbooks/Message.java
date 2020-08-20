package com.promise.gadsbooks;

public class Message {
    String topic;

    public Message() {
    }

    public Message(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
