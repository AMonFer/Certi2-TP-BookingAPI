package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.BookingEndpoints;
import entities.Booking;
import entities.BookingDates;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Request;

import java.util.List;

import static org.hamcrest.Matchers.not;

public class BookingSteps {
    Response response;

    @Given("I perform a GET call to the booking endpoint with id {string}")
    public void getBookingById(String id){
        response = Request.getById(BookingEndpoints.GET_BOOKING, id);
    }

    @And("I verify that the body does not have size {int}")
    public void verifyResponseSize(int size){
        response.then().assertThat().body("size()", not(size));
    }

    @And("I verify that the status code is {int}")
    public void verifyStatusCode(int statusCode){
        response.then().assertThat().statusCode(statusCode);
    }

    @Given("I perform a POST call to the booking endpoint with the following data")
    public void postBooking(DataTable bookingInfo) throws JsonProcessingException{

        List<String> data = bookingInfo.transpose().asList(String.class);

        Booking booking = new Booking();
        booking.setFirstname(data.get(0));
        booking.setLastname(data.get(1));
        booking.setTotalprice(Integer.parseInt(data.get(2)));
        booking.setDepositpaid(Boolean.parseBoolean(data.get(3)));

        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin(data.get(4));
        bookingdates.setCheckout(data.get(5));
        booking.setBookingdates(bookingdates);

        booking.setAdditionalneeds(data.get(6));


        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);

        response = Request.post(BookingEndpoints.POST_BOOKING, payload);

    }

    @Given("I perform a POST call to the booking endpoint with the following data but a bad json")
    public void postWithBadSintaxisJSON(DataTable bookingInfo) throws JsonProcessingException {
        List<String> data = bookingInfo.transpose().asList(String.class);

        Booking booking = new Booking();
        booking.setFirstname(data.get(0));
        booking.setLastname(data.get(1));
        booking.setTotalprice(Integer.parseInt(data.get(2)));
        booking.setDepositpaid(Boolean.parseBoolean(data.get(3)));

        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin(data.get(4));
        bookingdates.setCheckout(data.get(5));
        booking.setBookingdates(bookingdates);

        booking.setAdditionalneeds(data.get(6));


        ObjectMapper mapper = new ObjectMapper();
        String correctJson = mapper.writeValueAsString(booking);

        //Quito el ultimo caracter
        String incorrectJson = correctJson.substring(0, correctJson.length() - 1);
        response = Request.post(BookingEndpoints.POST_BOOKING, incorrectJson);




    }

    @Given("I perform A POST call with nothing")
    public void postBookingWithNothing(){
        String payload = "";
        response = Request.post(BookingEndpoints.POST_BOOKING, payload);
    }



}
