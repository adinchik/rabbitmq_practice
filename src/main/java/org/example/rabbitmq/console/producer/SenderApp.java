package org.example.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class SenderApp {
    private final static String QUEUE_NAME = "messages";
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            Scanner sc = new Scanner(System.in);
            while (true) {
                String s = sc.nextLine();
                int index = s.indexOf(" ");
                String language = s.substring(0, index);
                s = s.substring(index + 1);
                channel.basicPublish(EXCHANGE_NAME, language, null, s.getBytes("UTF-8"));
            }
        }
    }
}
