package org.eci.arep;

@RestController
public class HomeController {
   @GetMapping("/hello")
   public static String hello(@RequestParam(value = "name", defaultValue = "world") String name){
       return "greeting from microframework to " + name;
   }
}
