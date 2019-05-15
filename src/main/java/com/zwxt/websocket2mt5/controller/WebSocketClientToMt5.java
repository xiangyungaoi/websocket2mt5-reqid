package com.zwxt.websocket2mt5.controller;

import com.rabbitmq.client.Channel;
import com.zwxt.websocket2mt5.config.RabbitMqConfig;
import lombok.Data;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Created by gaoyx on 2019/4/26.
 */
@Data
@Component
public class WebSocketClientToMt5 {
    static Logger log = LoggerFactory.getLogger(WebSocketClientToMt5.class);
    @Autowired
    WebSocketClient webSocketClient;
    @Autowired
    public  RabbitTemplate rabbitTemplate;


    /**从mq中获取到前端传递的参数,并想mt5发送websocket请求
     * @param msg  从mq中获取到的消息
     * @param channel 与mq连接的信道
     * @param tag  相当于消息编号，用来标识消息
     */
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PARAMETER)
    public void getParameterFromMq(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        try {
            log.info("从mq的请求参数队列中接受到的请求参数:" + msg);
            //使用websocket想mt5发送消息
            webSocketClient.send(msg);
           /* 手动应答,第一个参数：确认消费的消息编号。 第二个参数:为 true 则表示拒绝 deliveryTag 编号之前所
            有未被当前消费者确认的消息。 这里应该优化，设置mq重新发送次数，超过重发次数，放入死信队列中*/
            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }












}
