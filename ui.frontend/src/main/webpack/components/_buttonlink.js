// Example of how a component should be initialized via JavaScript
// This script logs the value of the component's text property model message to the console

(function() {
    "use strict";

/*   function HelloWorld() {
	   if (console && console.log) {
                console.log(
                    "HelloWorld component JavaScript example",
                    "\nText property:\n",
                    "\nModel message:\n"
                );
            }
   }*/
   
   
   let number = 1;
   
   var div = document.querySelector(".cmp-buttonlink");
   div.onclick = () => {
   		number = number +1;
   		console.log(number)
   		var div = document.querySelector("#h1").innerHTML = number.toString();
   }
   
   return {
        number: number
    }
   
   /*if (document.readyState !== "loading") {
	  new HelloWorld();
   }else {
        document.addEventListener("DOMContentLoaded", HelloWorld);
    }*/
}());
