package com.kalix.trying.event.biz;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.Bean;

public class CamelRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:trigger?delay=500").transform().simple("ref:myBean").to("log:out");
    }

    @Bean(ref = "myBean")
    String myBean(){
        return "i am a bena!";
    }
}
