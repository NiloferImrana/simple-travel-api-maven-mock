package com.afkl.exercises.spring;


import com.afkl.exercises.spring.fares.Fare;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/client")
public class clientController {

 @Autowired
 MetricService metricService;

 @RequestMapping(value = "/{originUI}/{destinationUI}", method = RequestMethod.GET)
 @ResponseBody
 public void getFareDetails(@PathVariable ("originUI")  String originCode,
                            @PathVariable ("destinationUI") String destinationCode) throws IOException {

  System.out.println("origin and destination value********"+ originCode+"-------"+destinationCode);

//  RestTemplate restTemplate = new RestTemplate();
//
//  String credentials = "travel-api-client:psw";
//  String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//  HttpHeaders headers = new HttpHeaders();
//  headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//  headers.add("Authorization", "Basic " + encodedCredentials);
//
//  HttpEntity<String> request = new HttpEntity<>(headers);
//
//  String access_token_url = "http://localhost:8080/oauth/token";
//  access_token_url += "?grant_type=client_credentials";
//
//
//  System.out.println("Access Token request ---------" + request.toString());
//  ResponseEntity<String> response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
//
//  System.out.println("Access Token Response ---------" + response.getBody());
//
//// Get the Access Token From the recieved JSON response
//  ObjectMapper mapper = new ObjectMapper();
//  JsonNode node = mapper.readTree(response.getBody());
//  String token = node.path("access_token").asText();
//
  String url = "http://localhost:8080/fares/"+originCode+"/"+destinationCode+"?currency=USD";
//
//  // Use the access token for authentication
//  HttpHeaders headers1 = new HttpHeaders();
//  headers1.add("Authorization", "Bearer " + token);
//  HttpEntity<String> entity = new HttpEntity<>(headers1);
//
//  ResponseEntity<String> fare = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//  System.out.println("fare Response ---------" + fare.getBody());

 traverseRequest(url);

 }


// @RequestMapping(value = "/metric-details", method = RequestMethod.GET)
// public void getMetricData() {
//
////        ArrayList<Integer> statusMetricsByMinute = metricService.updateMetrics();
////        System.out.println("Status list"+statusMetricsByMinute.size());
//
//  metricService.initializeStatuses();
// }
//
// @RequestMapping(value = "/metric-graph-data", method = RequestMethod.GET)
// @ResponseBody
// public Object[][] getMetricDataFinal() {
//  return metricService.getGraphData();
// }
//
// @RequestMapping(value = "/index", method = RequestMethod.GET)
// public void list() {
//
//
////  String url1 = "http://localhost:8080/index";
////
////  // Use the access token for authentication
////  HttpHeaders headers1 = new HttpHeaders();
////  headers1.add("Authorization", "Bearer " + token1);
////  HttpEntity<String> uirequest = new HttpEntity<>(headers1);
//
// // restTemplateUI.exchange(url1, HttpMethod.GET, uirequest, String.class);
//
// }

 public String traverseRequest(String URL) throws IOException {

  System.out.println("inside travel request******"+"UR_____"+URL);
  RestTemplate restTemplate = new RestTemplate();

  String credentials = "travel-api-client:psw";
  String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

  HttpHeaders headers = new HttpHeaders();
  headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
  headers.add("Authorization", "Basic " + encodedCredentials);

  HttpEntity<String> request = new HttpEntity<>(headers);

  String access_token_url = "http://localhost:8080/oauth/token";
  access_token_url += "?grant_type=client_credentials";


  System.out.println("Access Token request ---------" + request.toString());
  ResponseEntity<String> response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

  System.out.println("Access Token Response ---------" + response.getBody());

// Get the Access Token From the recieved JSON response
  ObjectMapper mapper = new ObjectMapper();
  JsonNode node = mapper.readTree(response.getBody());
  String token = node.path("access_token").asText();

  //String url = "http://localhost:8080/fares/LWS/SEA?currency=USD";

  // Use the access token for authentication
  HttpHeaders headers1 = new HttpHeaders();
  headers1.add("Authorization", "Bearer " + token);
  HttpEntity<Fare> entity = new HttpEntity<Fare>(headers1);

ResponseEntity<Fare> result= restTemplate.exchange(URL, HttpMethod.GET, entity, Fare.class);

return result.toString();

 }

 @RequestMapping(value = "/metric-graph-data", method = RequestMethod.GET)
 @ResponseBody
 public Object[][] getMetricData() {
  return metricService.getGraphData();
 }

}