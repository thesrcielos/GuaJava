package org.eci.arep;

@RestController
public class TempController {

    @GetMapping("/temp")
    public static String temp(@RequestParam(value = "msg", defaultValue = "default") String msg) {
        return "Temp says: " + msg;
    }
}