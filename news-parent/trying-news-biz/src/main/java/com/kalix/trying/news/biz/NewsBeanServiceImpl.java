package com.kalix.trying.news.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.Assert;
import com.kalix.trying.news.api.biz.INewsBeanService;
import com.kalix.trying.news.api.dao.INewsBeanDao;
import com.kalix.trying.news.entities.NewsBean;
import org.apache.commons.lang.StringUtils;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.fusesource.mqtt.codec.MQTTFrame;

import java.net.URISyntaxException;

/**
 * @类描述：
 * @创建人：
 * @创建时间：
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class NewsBeanServiceImpl extends ShiroGenericBizServiceImpl<INewsBeanDao, NewsBean> implements INewsBeanService {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void beforeSaveEntity(NewsBean entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setPublishPeople(userName);
        }
        super.beforeSaveEntity(entity, status);
    }

    @Override
    public void mqtt_publish(String msg) {

    }

    @Override
    public void mqtt_subscribe(String topic) {

    }

    public void init() {
        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost("106.12.199.69", 1883);
            //使用回调式API
            final CallbackConnection callbackConnection = mqtt.callbackConnection();
            //发布消息
            callbackConnection.publish("foo", ("Hello ").getBytes(), QoS.AT_LEAST_ONCE, true, new Callback<Void>() {
                public void onSuccess(Void v) {
                    System.out.println("===========消息发布成功============");
                }

                public void onFailure(Throwable value) {
                    System.out.println("========消息发布失败=======");
                    callbackConnection.disconnect(null);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost("106.12.199.69", 1883);
            //设置跟踪器
            mqtt.setTracer(new Tracer() {
                @Override
                public void onReceive(MQTTFrame frame) {
                    System.out.println("recv: " + frame);
                }

                @Override
                public void onSend(MQTTFrame frame) {
                    System.out.println("send: " + frame);
                }

                @Override
                public void debug(String message, Object... args) {
                    System.out.println(String.format("debug: " + message, args));
                }
            });

            //使用回调式API
            final CallbackConnection callbackConnection = mqtt.callbackConnection();

            //连接
            callbackConnection.connect(new Callback<Void>() {

                //连接失败
                public void onFailure(Throwable value) {
                    System.out.println("============连接失败：" + value.getLocalizedMessage() + "============");
                }

                // 连接成功
                @Override
                public void onSuccess(Void v) {


                    //订阅主题
                    Topic[] topics = {new Topic("foxtrackr/1BBCE/v1.0/bat", QoS.AT_LEAST_ONCE)};
                    callbackConnection.subscribe(topics, new Callback<byte[]>() {
                        @Override
                        public void onSuccess(byte[] o) {
                            System.out.println("========订阅成功=======");
                        }

                        //订阅主题失败
                        public void onFailure(Throwable value) {
                            System.out.println("========订阅失败=======");
                            callbackConnection.disconnect(null);
                        }
                    });
                }
            });

            //连接监听
            callbackConnection.listener(new Listener() {

                //接收订阅话题发布的消息
                @Override
                public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
                    System.out.println("=============receive msg================" + new String(payload.toByteArray()));
                    ack.run();
                }

                //连接失败
                @Override
                public void onFailure(Throwable value) {
                    System.out.println("===========connect failure===========");
                    callbackConnection.disconnect(null);
                }

                //连接断开
                @Override
                public void onDisconnected() {
                    System.out.println("====mqtt disconnected=====");

                }

                //连接成功
                @Override
                public void onConnected() {
                    System.out.println("====mqtt connected=====");

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

