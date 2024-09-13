import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.not;

public class RestAssuredCrud {

    @Test
    public void getEmployeesTest(){
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        Response response = RestAssured
                .when().get("/employees");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("size()", not(0));
        response.then().log().body();
    }

    @Test
    public void getEmployeeByIdTest(){
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        Response response = RestAssured
                .given().pathParam("id", "1")
                .when().get("/employees/{id}");
        response.then().assertThat().statusCode(200);
        response.then().log().body();
        response.then().assertThat().body("data.id", Matchers.equalTo(1));
        response.then().assertThat().body("data.employee_name", Matchers.equalTo("Tiger Nixon"));
    }

    @Test
    public void postEmployeeTest() throws JsonProcessingException {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        Employee employee = new Employee();
        employee.setName("Mauricio");
        employee.setSalary("3000");
        employee.setAge("28");


        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(employee);
        System.out.println(payload);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload)
                .when().post("/create");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("data.name", Matchers.equalTo(employee.getName()));
        response.then().assertThat().body("data.salary", Matchers.equalTo(employee.getSalary()));
        response.then().assertThat().body("data.age", Matchers.equalTo(employee.getAge()));
        response.then().log().body();
    }

    @Test
    public void putEmployeeTest() throws JsonProcessingException {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        Employee employee = new Employee();
        employee.setName("Updated");
        employee.setSalary("3000");
        employee.setAge("28");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(employee);
        System.out.println(payload);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload)
                .and().pathParam("id", "1")
                .when().put("/update/{id}");

        response.then().log().body();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("message", Matchers.equalTo("Successfully! Record has been updated."));

        response.then().assertThat().body("data.name", Matchers.equalTo(employee.getName()));
        response.then().assertThat().body("data.salary", Matchers.equalTo(employee.getSalary()));
        response.then().assertThat().body("data.age", Matchers.equalTo(employee.getAge()));
    }

    @Test
    public void deleteEmployee(){
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        Response responde = RestAssured
                .given().pathParam("id", "2")
                .when().delete("/delete/{id}");

        responde.then().log().body();
        responde.then().assertThat().statusCode(200);
        responde.then().assertThat().body("data", Matchers.equalTo("2"));
        responde.then().assertThat().body("message", Matchers.equalTo("Successfully! Record has been deleted"));
    }
}

