package com.example.amzscout;

import org.apache.commons.lang3.mutable.MutableLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.net.HttpHeaders.X_FORWARDED_FOR;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Component
public class ThrottlingCache {

    private final double timeForRequest;
    private final Map<String, MutableLong> ipToLastRequest;

    @Autowired
    public ThrottlingCache(@Value("${requests}") Integer requests, @Value("${seconds}") Integer seconds,
                           @Value("${numberOfIps}") Integer numberOfIps) {
        this.timeForRequest = seconds / 1.0 * requests * 1000000000;
        this.ipToLastRequest = new ConcurrentHashMap<>(numberOfIps);
    }

    public boolean putRequest(HttpServletRequest request) {
        boolean result;
        MutableLong lastRequestArrivalTime = ipToLastRequest.get(ofNullable(request.getHeader(X_FORWARDED_FOR)).orElse(""));
        if (lastRequestArrivalTime == null) {
            lastRequestArrivalTime = new MutableLong(0);
            ipToLastRequest.put(ofNullable(request.getHeader(X_FORWARDED_FOR)).orElse(""), lastRequestArrivalTime);
        }
        long currentTime = System.nanoTime();
        result = !(currentTime - lastRequestArrivalTime.getValue() < timeForRequest);
        lastRequestArrivalTime.setValue(currentTime);
        return result;
    }
}
