package com.myorgname.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;


public class Wrapper {

    	public void sendMsg(String hostName, String queueName, byte[] msg) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostName);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(queueName, false, false, false, null);

		channel.basicPublish("", queueName, null, msg);

		channel.close();
		connection.close();

    	}

	public int readMsg(String hostName, String queueName, byte[] msg) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostName);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(queueName, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();      
		int len = delivery.getBody().length;
		System.arraycopy(delivery.getBody(),0,msg,0,len);
		
		channel.close();
		connection.close();

		return len;

	} 

}

