package com.zwxt.websocket2mt5.util.mq;

import com.zwxt.websocket2mt5.config.WebSocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Created by gaoyx on 2019/4/26.
 */
public class Send2Mq {
    private static Logger log = LoggerFactory.getLogger(Send2Mq.class);

    public static void sendObject2Mq(String info, String exchange, String routingkey, RabbitTemplate rabbitTemplate){
            rabbitTemplate.convertAndSend(exchange, routingkey, info, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //设置消息的属性message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT)可以让消息持久化,Springboot默认就是持久化了，可以省略不写
                    //持久化消息并且以String的方式保存
                    message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                    return message;
                }
            });
            //确认生产者是否将消息成功发送到mq上的交换器(已经使用了备份交互器，mandatory 参数无效，所以这里可以不写)
           /* rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    if (ack) {
                        log.info("消息成功发送到mq交换器." + "相关的数据" + correlationData);
                    }else {
                        log.info("消息发送到mq交换器失败" + "原因:" + cause);
                    }
                }
             });*/
    }
}
