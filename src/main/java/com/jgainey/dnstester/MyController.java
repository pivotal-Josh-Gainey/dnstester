package com.jgainey.dnstester;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Controller
public class MyController {


    @RequestMapping(method = RequestMethod.GET, value = "/start", produces = "application/json")
    public ResponseEntity<String> start(){

        String uri = System.getenv("URI");


        HttpClient httpClient  = HttpClient.newConnection().resolver(DefaultAddressResolverGroup.INSTANCE).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection-> connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        WebClient webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).baseUrl(uri.trim()).build();

        for(int i = 0 ; i<1 ; i++){
            System.out.println("Starting loop iteration " + i);
            String result = webClient.get().retrieve().bodyToMono(String.class).block();
            System.out.println("Result = " + result);
            System.out.println("Complete loop iteration " + i);
        }

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.GET, value = "/start2", produces = "application/json")
    public ResponseEntity<String> start2(){

        for(int i = 0 ; i<1 ; i++){
            System.out.println("Starting loop iteration " + i);
            try{
                String uri = "https://"+System.getenv("URI");

                URL url = new URL(uri);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();
                System.out.println("Value of responsecode = " + responseCode);
            }catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Complete loop iteration " + i);
        }

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
