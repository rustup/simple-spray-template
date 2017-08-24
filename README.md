

## Simple-Spray-Template

	 Provide Some simple examples to help using Spray Web Framework with twirl and slick.

	1> Use twirl as front template engine. Html files need to be placed in a directory named twirl. For example
	   src/scala/main/twirl/packagePrefix/object/func.scala.html 
		 Fill the func.scala.html as follow
		 ```
		 @(name: String, age: Int = 42)
		 <html>
		     @* This template is a simple html template --- (this text btw. is a comment and is not rendered) *@
				     <h1>Welcome @name!!</h1>
						     <p>You are @age years old, @(if(age < 21) <i>shouldn't you be in bed ?!</i> else <i>have a great evening !</i>)</p>
								 </html>
		 ```

		 Call packagePrefix.object.func(arg1,arg2) to render func.scala.html

  2> Http route
	   *  Response with html with packagePrefix.object.func(arg1,arg2).toString
		 *  Use MyJsonProtocol to respond json content
		 
	3> CRUD 
	   Slick provides Table to map table in db, TableQuery to do crud. Await and Future to do IO operations.
		 
		  
	   
