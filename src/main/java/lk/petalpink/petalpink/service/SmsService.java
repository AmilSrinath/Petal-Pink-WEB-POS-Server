package lk.petalpink.petalpink.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    private static final String API_URL       = "https://api.textit.biz/";
    private static final String API_KEY       = "228fgkd160f1fdc98dtd26a0adh8847";
    private static final String TRACKING_BASE = "https://petalpink.lk/order-tracking/";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Order confirm SMS send කරනවා.
     * Order already committed වූ පසු call කරන නිසා exception throw නොකරයි — log පමණයි.
     */
    public void sendOrderConfirmation(String phone, String orderRef) {
        System.out.println("In SMS");
        if (phone == null || phone.isBlank()) {
            log.warn("SMS skipped — phone number is empty for order {}", orderRef);
            System.out.println("Skip SMS");
            return;
        }

        String trackingLink = TRACKING_BASE + orderRef;
        String message = "Your order has been placed successfully! " +
                         "Order ID: #" + orderRef + ". ";

        String body = """
                {"to":"%s","text":"%s"}
                """.formatted(phone, message);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("X-API-VERSION", "v1")
                    .header("Authorization", "Basic " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            log.info("SMS sent to {} for order {} — status: {}, response: {}",
                    phone, orderRef, response.statusCode(), response.body());

        } catch (Exception e) {
            // Order already saved — SMS failure should not break the flow
            System.out.println("Failed SMS");
            log.error("Failed to send SMS to {} for order {}: {}", phone, orderRef, e.getMessage());
        }
    }
}
