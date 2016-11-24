package com.rest.test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.response.ResponseOptions;

import org.junit.Before;
import org.junit.Test;


public class LoginTest {
	
	 String cookieValue="";
	@Before
	public void setUP(){
		
	   //指定 URL 和端口号
	   RestAssured.baseURI = "http://www.k6kt.com";
	   RestAssured.port = 80;
	   ResponseOptions res= given().param("name", "siri").param("pwd", "1881").when().get("/user/login.do");

	   cookieValue= res.getCookie("ui");
	 }

	//hasItems
     @Test
	public void testGETFriendBlog()
	{
    	 given().
			    cookies("ui",cookieValue)
			    .param("page", 2)
			    .param("pageSize", 12)
			    .param("hottype", 1)
			    .param("seachtype", 1)
			    .param("blogtype", 1)
			    .param("theme", "")
			 .when()
			    .get("homeschool/selFriendBlogInfo.do")
			 .then()
			  .log()
			  .all()
			  .body(
					  "total", equalTo(170),
					  "rows[0].id", equalTo("576a52ca0cf296df40c89c7f"),
					  "page",equalTo(2)
				   )
			  .body("rows[0].id", equalTo("576a52ca0cf296df40c89c7f"));
	}
		 
 		 
	 
}
