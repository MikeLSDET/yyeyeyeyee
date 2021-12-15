package API;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCodedExamplesOfAPI {

    /*Given - pre condition- prepare the request
    When - action -sending request/hitting the endpoint
    Then- result-verify response*/



    String bassURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";

    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MzkzMzAwNjUsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTYzOTM3MzI2NSwidXNlcklkIjoiMzIyMiJ9.Ae3gEF684Hh3nEY2oDTbD_pEHpdAWl_HDmMss23OocA";

    static String employee_id;

    @Test
    public void dgetDetailsOfOneEmployee() {
        //rest assured considers baseurl as baseuri



        //given
        RequestSpecification preparedRequest = given().header("Authorization",token )
                .header("Content-Type", "application/json").queryParam("employee_id", "25251A");



        //when -hitting the endpoint
       Response response= preparedRequest.when().get("/getOneEmployee.php");

        System.out.println(response.asString());

        //then - result/assertion

        response.then().assertThat().statusCode(200);

    }
@Test
    public void acreateEmployee(){


        //given
       RequestSpecification preparedRequest= given().header("Authorization",token )
                .header("Content-Type", "application/json").body("{\n" +
                        "  \"emp_firstname\": \"Alu1222\",\n" +
                        "  \"emp_lastname\": \"ButtersWorth1\",\n" +
                        "  \"emp_middle_name\": \"PancakeHead1\",\n" +
                        "  \"emp_gender\": \"M\",\n" +
                        "  \"emp_birthday\": \"2000-12-04\",\n" +
                        "  \"emp_status\": \"Employee\",\n" +
                        "  \"emp_job_title\": \"API Tester\"}");

       //when
        Response response=preparedRequest.when().post("/createEmployee.php");

        //then
        //this pretty print does same job as syso. sout(response.asString());
        response.prettyPrint();


        //jsonPath() we use this to get specific information from the json object
       employee_id= response.jsonPath().getString("Employee.employee_id");
        System.out.println(employee_id);

        //then
        response.then().assertThat().statusCode(201);
        response.then().assertThat().body("Employee.emp_firstname",equalTo("Alu1222"));
        response.then().assertThat().body("Message",equalTo("Employee Created"));
        response.then().assertThat().header("Server","Apache/2.4.39 (Win64) PHP/7.2.18");
    }


    public void bgetCreatedEmployee(){
        RequestSpecification preparedRequest= given().header("Authorization",token )
                .header("Content-Type", "application/json").queryParam("employee_id", employee_id);


        Response response=preparedRequest.when().get("/getOneEmployees.php");


        String empID=response.jsonPath().getString("Employee.employee_id");


       boolean comparingEmpID = empID.contentEquals(employee_id);

        Assert.assertTrue(comparingEmpID);

        String firstName=response.jsonPath().getString("Employee.emp_firstname");

        Assert.assertTrue(firstName.contentEquals("Alu1222"));

        String lastname=response.jsonPath().getString("Employee.emp_lastname");
        Assert.assertTrue(lastname.contentEquals("ButtersWorth1"));




    }
@Test
    public void cupdatedCreatedEmployee(){
        RequestSpecification preparedRequest= given().header("Authorization",token )
                .header("Content-Type", "application/json").body("{\n" +
                        "  \"employee_id\": \"" + employee_id + "\",\n"+
                        "  \"emp_firstname\": \"Blueberry1\",\n" +
                        "  \"emp_lastname\": \"Butters1\",\n" +
                        "  \"emp_middle_name\": \"Pancake1\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"2001-12-04\",\n" +
                        "  \"emp_status\": \"Emp\",\n" +
                        "  \"emp_job_title\": \"Cloud Consultant\"\n+" +
                        "}");


        Response response=preparedRequest.when().put("/updateEmployee.php");
        response.prettyPrint();


    }


    @Test
    public void getAllEmployees(){
        RequestSpecification preparedRequest= given().header("Authorization",token )
                .header("Content-Type", "application/json");

        Response response=preparedRequest.when().get("/getAllEmployees.php");

        String allEmployees=response.prettyPrint();


        JsonPath js=new JsonPath(allEmployees);

        int count =js.getInt("Employees.size()");

        System.out.println(count);


        for(int i=0;i<count;i++){
            String employeeIds= js.getString("Employees["+i+"].employee_id");

            System.out.println(employeeIds);


        }
    }


}
