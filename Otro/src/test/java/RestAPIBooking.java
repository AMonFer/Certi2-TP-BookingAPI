import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Booking;
import entities.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

public class RestAPIBooking {

    //Prueba 1
    @Test
    public void getBookingByValidId(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Response response = RestAssured
                .given().pathParam("id", "5")
                .when().get("/booking/{id}");

        response.then().assertThat().statusCode(200);
        response.then().log().body();
        //Los id cambian cada cierto tiempo, por lo que no se puede tener una respuesta predeterminada que sea correcta
        //response.then().assertThat().body("firstname", Matchers.equalTo("Eric"));
        //response.then().assertThat().body("lastname", Matchers.equalTo("Brown"));
        //response.then().assertThat().body("totalprice", Matchers.equalTo(727));
    }

    //Prueba 3
    @Test
    public void getBookingByInvalidId(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Response response = RestAssured
                .given().pathParam("id", "letras")
                .when().get("/booking/{id}");

        response.then().assertThat().statusCode(404);
        response.then().log().body();
    }

    //Prueba 8
    @Test
    public void getBookingByInvalidFormatId(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Response response = RestAssured
                .given().pathParam("id", "cinco")
                .when().get("/booking/{id}");

        response.then().assertThat().statusCode(404);
        response.then().log().body();
    }

    //Prueba 4
    @Test
    public void postBookingWithValidData() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";


        Booking booking = new Booking();
        booking.setFirstname("Adrian");
        booking.setLastname("Montaño");
        booking.setTotalprice(100);
        booking.setDepositpaid(false);

        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin("2016-10-16");
        bookingdates.setCheckout("2019-07-06");
        booking.setBookingdates(bookingdates);

        booking.setAdditionalneeds("Breakfast");


        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);
        System.out.println(payload);


        Response response = RestAssured
                .given().contentType(ContentType.JSON).body(payload)
                .when().post("/booking");


        response.then().assertThat().statusCode(200);
        response.then().log().body();

    }

    //Prueba 5
    @Test
    public void postBookingWithNothing() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/booking");

        //Deberia devolver 400 bad request (esta devolviendo 500 internal server error)
        response.then().assertThat().statusCode(400);
        response.then().log().body();
    }

    //Prueba 12
    @Test
    public void postBookingWithBadSintaxisJson() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";


        Booking booking = new Booking();
        booking.setFirstname("Susan");
        booking.setLastname("Jackson");
        booking.setTotalprice(139);
        booking.setDepositpaid(false);

        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin("2016-10-16");
        bookingdates.setCheckout("2019-07-06");
        booking.setBookingdates(bookingdates);

        booking.setAdditionalneeds("Breakfast");


        ObjectMapper mapper = new ObjectMapper();
        String correctJson = mapper.writeValueAsString(booking);

        //Quito el ultimo caracter
        String incorrectJson = correctJson.substring(0, correctJson.length() - 1);


        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(incorrectJson)
                .when()
                .post("/booking");


        response.then().assertThat().statusCode(400);
        response.then().log().body();
    }

}
