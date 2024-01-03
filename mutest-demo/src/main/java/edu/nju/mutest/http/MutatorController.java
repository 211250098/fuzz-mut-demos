package edu.nju.mutest.http;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/api")
public class MutatorController {
    static String mutator;

    @PostMapping("/mutator")
    public ResponseEntity<String> MutatorUpload(@RequestBody String mut) {

        if (mut != null) {

            mutator = mut;
            System.out.println(mutator);

            return new ResponseEntity<>("Mutator uploaded successfully", HttpStatus.OK);

        }
        return new ResponseEntity<>("Mutator is empty", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/mutator/{id}")
    public  ResponseEntity<String> updateResource(@PathVariable("id") String resourceId, @RequestBody String mut) {

        if (mut != null) {
            mutator = mut;

            return new ResponseEntity<>("Mutator uploaded successfully", HttpStatus.OK);

        }
        return new ResponseEntity<>("Mutator is empty", HttpStatus.BAD_REQUEST);    }



    public static String getMutator(){
        return mutator;
    }


}
