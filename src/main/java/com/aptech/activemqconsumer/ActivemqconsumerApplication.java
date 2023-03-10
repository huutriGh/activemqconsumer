package com.aptech.activemqconsumer;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

@SpringBootApplication
public class ActivemqconsumerApplication implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(ActivemqconsumerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ActivemqconsumerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		logger.info("Create a ConnectionFactory");

		ConnectionFactory connectionFactory = new JmsConnectionFactory("amqp://localhost:56722");

		Connection connection = connectionFactory.createConnection("admin", "admin");
		connection.start();

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Destination destination = null;

		destination = session.createQueue("jms-queue");

		MessageConsumer consumer = session.createConsumer(destination);

		logger.info("Start receiving messages ... ");
		String body;
		do {
			Message msg = consumer.receive();
			body = ((TextMessage) msg).getText();
			logger.info("Received = " + body);
		} while (!body.equalsIgnoreCase("close"));

		logger.info("Shutdown JMS connection and free resources");
		connection.close();

	}

}
