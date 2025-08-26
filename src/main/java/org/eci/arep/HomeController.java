/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.eci.arep;

@RestController
public class HomeController {
   @GetMapping("/hello")
   public static String hello(@RequestParam(value = "name") String name){
       return "greeting from microframework " + name;
   }
}
