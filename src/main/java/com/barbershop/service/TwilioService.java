package com.barbershop.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final String ACCOUNT_SID = System.getenv("TWILIO_SID");
    private final String AUTH_TOKEN = System.getenv("TWILIO_TOKEN");
    private final String FROM_NUMBER = System.getenv("TWILIO_PHONE");

    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void enviarSms(String to, String mensaje) {
        Message.creator(
            new PhoneNumber(to),
            new PhoneNumber(FROM_NUMBER),
            mensaje
        ).create();
    }
}
