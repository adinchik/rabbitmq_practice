package org.example.rabbitmq.console.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ReceiverApp {
    private static final String EXCHANGE_NAME = "directExchanger";
    private final static String QUEUE_NAME = "messages";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner sc = new Scanner(System.in);
        System.out.println(" [*] Waiting for messages");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'" + "from topic about " + delivery.getEnvelope().getRoutingKey());
            System.out.println(Thread.currentThread().getName());
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });

        while (true) {
            String s = sc.nextLine();
            int index = s.indexOf(" ");
            String language = s.substring(index + 1);
            s = s.substring(0, index);
            if (s.equals("set_topic")) {
                channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, language);
                System.out.println("added language " + language);
            }
            else if (s.equals("delete_topic")) {
                channel.queueUnbind(QUEUE_NAME, EXCHANGE_NAME, language);
                System.out.println("deleted language " + language);
            }
        }

    }
}
