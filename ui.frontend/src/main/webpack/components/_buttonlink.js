/*// Example of how a component should be initialized via JavaScript
// This script logs the value of the component's text property model message to the console

(function() {
    "use strict";
    
    $( document ).ready(function() {
		let number = 1;
	   $(".cmp-buttonlink").on("click", function() {
		   number = number +1;
		   console.log(number)
		   $("#h1").html(number.toString())
	   })
	})
}());
*/