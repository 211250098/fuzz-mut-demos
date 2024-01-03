package edu.nju.mutest.http;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class MutResController {
    @GetMapping(path="/result")
    @ResponseStatus(HttpStatus.CREATED)
    public String GetResult(@RequestBody String res){
        System.out.println("Upload res");
        return res;
    }

}
