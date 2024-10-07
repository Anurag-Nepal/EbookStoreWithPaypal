package com.anuragnepal.itbooksnepal.Paypal;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartDto;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartItemDto;
import com.anuragnepal.itbooksnepal.Services.CartServices;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class CheckOut {
    @Autowired
    CartServices cartServices;

    @Value("${PAYPAL_CLIENT_ID}")
    private String PAYPAL_CLIENT_ID;

    @Value("${PAYPAL_CLIENT_SECRET}")
    private String PAYPAL_CLIENT_SECRET;
    private final String BASE_URL = "https://api-m.sandbox.paypal.com";
    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public APIContext apiContext() {
        return new APIContext(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET, mode);
    }


    public Map<String, Object> createPayment() {
        Map<String, Object> response = new HashMap<String, Object>();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(cartServices.showCartOfUser().getPrice().toString());
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Item> items = new ArrayList<>();
        for(CartItemDto cartDto:cartServices.showCartOfUser().getCartList())
        {
            Item item = new Item();
            item.setCurrency("USD");
            item.setName(cartDto.getBooks().getName());
            item.setPrice(cartDto.getBooks().getPrice().toString());
            item.setQuantity("1");
            items.add(item);
        }
        ItemList itemList = new ItemList(); // Create an empty ItemList
        itemList.setItems(items); // Use setter method to set items in the ItemList
        transaction.setItemList(itemList); // Set the item list in the transaction
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/complete/payment");
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            APIContext context = new APIContext(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET, mode);
            createdPayment = payment.create(context);
            if (createdPayment != null) {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
                response.put("paymentId",createdPayment.getId());

            }
        } catch (PayPalRESTException e) {
            System.out.println("Error happened during payment creation!");
        }
        return response;
    }

    public Map<String, Object> completePayment(HttpServletRequest req){
        Map<String, Object> response = new HashMap();
        Payment payment = new Payment();
        payment.setId(req.getParameter("paymentId"));
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("PayerID"));
        try {
            APIContext context = new APIContext(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET, mode);
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment!=null){
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return response;
    }
}

    





















































































//
//    public String generateToken()throws IOException
//    {
//        if (PAYPAL_CLIENT_ID == null || PAYPAL_CLIENT_SECRET == null) {
//            throw new IllegalArgumentException("Invalid Credentials.Can't make Payment To This");
//        }
//        String auth = Base64.getEncoder().encodeToString((PAYPAL_CLIENT_ID +":"+PAYPAL_CLIENT_SECRET).getBytes());
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization","Basic "+auth);
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap< String, String > body = new LinkedMultiValueMap< >();
//        body.add("grant_type", "client_credentials");
//
//        ResponseEntity<JsonNode> response = restTemplate.postForEntity(BASE_URL + "/v1/oauth2/token", new HttpEntity< >(body, headers), JsonNode.class);
//        return response.getBody().get("access_token").asText();
//
//    }
//
//    public ResponseEntity<JsonNode> createOrder( CartDto cartDto) {
//        try {
//            JsonNode response = createPaypalOrder(cartDto);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//    public JsonNode createPaypalOrder(CartDto cart) throws IOException {
//        String accessToken = generateToken();
//        String url = BASE_URL + "/v2/checkout/orders";
//
//        // Create payload for the order
//        ObjectNode payload = objectMapper.createObjectNode();
//        payload.put("intent", "CAPTURE");
//
//        // Create purchase units array
//        ObjectNode purchaseUnits = payload.putArray("purchase_units").addObject();
//        ObjectNode amount = purchaseUnits.putObject("amount");
//        amount.put("currency_code", "USD");
//
//        // Get total price directly from CartDto
//        BigDecimal totalAmount = BigDecimal.valueOf(cart.getPrice());
//        amount.put("value", totalAmount.toString());
//
//        // Create the items array and populate it
//        ArrayNode itemsArray = purchaseUnits.putArray("items");
//        for (CartItemDto item : cart.getCartList()) {
//            ObjectNode itemNode = itemsArray.addObject();
//            itemNode.put("name", item.getBooks().getName());
//            itemNode.put("description", item.getBooks().getDescription()); // Add description if available
//
//            // Set unit_amount correctly
//            ObjectNode unitAmount = itemNode.putObject("unit_amount");
//            unitAmount.put("currency_code", "USD");
//            unitAmount.put("value", item.getBooks().getPrice().toString());
//
//            itemNode.put("quantity", 1); // Use the actual quantity from the item
//            ObjectNode breakdown = amount.putObject("breakdown");
//            ObjectNode itemTotal = breakdown.putObject("item_total");
//            itemTotal.put("currency_code", "USD");
//            itemTotal.put("value", totalAmount.toString());
//
//
//        }
//
//
//        ObjectNode applicationContext = objectMapper.createObjectNode();
//        applicationContext.put("return_url", "http://localhost:8080/success");
//        applicationContext.put("cancel_url", "http://localhost:8080/cancel");
//        payload.set("application_context", applicationContext);
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), JsonNode.class);
//        JsonNode jsonNod = handleResponse(response);
//        return jsonNod;
//    }
//
//    private JsonNode handleResponse(ResponseEntity<JsonNode> response) {
//        // Check if the response status code indicates success
//        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
//            // Return the body of the response if successful
//            return response.getBody();
//        } else {
//            // Handle errors based on status code
//            String errorMessage = "Failed to create order: " + response.getStatusCode() + " - " + response.getBody();
//
//            throw new RuntimeException(errorMessage);
//        }
//    }
//
//
//    public ResponseEntity < JsonNode > captureOrder(String orderID) {
//        try {
//
//            JsonNode response = captureOrders(orderID);
//            return new ResponseEntity < > (response, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity < > (HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private JsonNode captureOrders(String orderID) throws IOException {
//        String accessToken = generateToken();
//        String url = BASE_URL + "/v2/checkout/orders/" + orderID + "/capture";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        ResponseEntity < JsonNode > response = restTemplate.postForEntity(url, new HttpEntity < > (headers), JsonNode.class);
//        createPaypalOrder(cartServices.showCartOfUser()).get("id").asText();
//        return handleResponse(response);
//    }
//




     



















