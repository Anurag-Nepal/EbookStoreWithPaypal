package com.anuragnepal.itbooksnepal.Paypal;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartDto;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartItemDto;
import com.anuragnepal.itbooksnepal.Services.CartServices;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.Base64;

import static java.lang.reflect.Array.set;

@Service
public class CheckOut {
    @Autowired
    CartServices cartServices;

    @Value("${PAYPAL_CLIENT_ID}")
    private String PAYPAL_CLIENT_ID;

    @Value("${PAYPAL_CLIENT_SECRET}")
    private String PAYPAL_CLIENT_SECRET;

    private final String BASE_URL = "https://api-m.sandbox.paypal.com";


    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public CheckOut(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public String generateToken()throws IOException
    {
        if (PAYPAL_CLIENT_ID == null || PAYPAL_CLIENT_SECRET == null) {
            throw new IllegalArgumentException("Invalid Credentials.Can't make Payment To This");
        }
        String auth = Base64.getEncoder().encodeToString((PAYPAL_CLIENT_ID +":"+PAYPAL_CLIENT_SECRET).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Basic "+auth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap< String, String > body = new LinkedMultiValueMap< >();
        body.add("grant_type", "client_credentials");

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(BASE_URL + "/v1/oauth2/token", new HttpEntity< >(body, headers), JsonNode.class);
        return response.getBody().get("access_token").asText();

    }

    public ResponseEntity<JsonNode> createOrder( CartDto cartDto) {
        try {
            JsonNode response = createPaypalOrder(cartDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public JsonNode createPaypalOrder(CartDto cart) throws IOException {
        String accessToken = generateToken();
        String url = BASE_URL + "/v2/checkout/orders";

        // Create payload for the order
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("intent", "CAPTURE");

        // Create purchase units array
        ObjectNode purchaseUnits = payload.putArray("purchase_units").addObject();
        ObjectNode amount = purchaseUnits.putObject("amount");
        amount.put("currency_code", "USD");

        // Get total price directly from CartDto
        BigDecimal totalAmount = BigDecimal.valueOf(cart.getPrice());
        amount.put("value", totalAmount.toString());

        // Create the items array and populate it
        ArrayNode itemsArray = purchaseUnits.putArray("items");
        for (CartItemDto item : cart.getCartList()) {
            ObjectNode itemNode = itemsArray.addObject();
            itemNode.put("name", item.getBooks().getName());
            itemNode.put("description", item.getBooks().getDescription()); // Add description if available

            // Set unit_amount correctly
            ObjectNode unitAmount = itemNode.putObject("unit_amount");
            unitAmount.put("currency_code", "USD");
            unitAmount.put("value", item.getBooks().getPrice().toString());

            itemNode.put("quantity", 1); // Use the actual quantity from the item
            ObjectNode breakdown = amount.putObject("breakdown");
            ObjectNode itemTotal = breakdown.putObject("item_total");
            itemTotal.put("currency_code", "USD");
            itemTotal.put("value", totalAmount.toString());


        }


        ObjectNode applicationContext = objectMapper.createObjectNode();
        applicationContext.put("return_url", "http://localhost:8080/success");
        applicationContext.put("cancel_url", "http://localhost:8080/cancel");
        payload.set("application_context", applicationContext);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), JsonNode.class);
        JsonNode jsonNod = handleResponse(response);
        return jsonNod;
    }

    private JsonNode handleResponse(ResponseEntity<JsonNode> response) {
        // Check if the response status code indicates success
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
            // Return the body of the response if successful
            return response.getBody();
        } else {
            // Handle errors based on status code
            String errorMessage = "Failed to create order: " + response.getStatusCode() + " - " + response.getBody();

            throw new RuntimeException(errorMessage);
        }
    }


    public ResponseEntity < JsonNode > captureOrder(String orderID) {
        try {

            JsonNode response = captureOrders(orderID);
            return new ResponseEntity < > (response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity < > (HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private JsonNode captureOrders(String orderID) throws IOException {
        String accessToken = generateToken();
        String url = BASE_URL + "/v2/checkout/orders/" + orderID + "/capture";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity < JsonNode > response = restTemplate.postForEntity(url, new HttpEntity < > (headers), JsonNode.class);
        createPaypalOrder(cartServices.showCartOfUser()).get("id").asText();
        return handleResponse(response);
    }





        }



















