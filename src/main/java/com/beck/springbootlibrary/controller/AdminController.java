package com.beck.springbootlibrary.controller;


import com.beck.springbootlibrary.requestmodels.AddBookRequest;
import com.beck.springbootlibrary.service.AdminService;
import com.beck.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value = "Authorization") String token, @RequestBody AddBookRequest addBookRequest) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if (admin == null || !admin.equals("admin")){
            throw new Exception("Adminstration page only");
        }
        adminService.postBook(addBookRequest);
    }

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if (admin == null || !admin.equals("admin")){
            throw new Exception("Adminstration page only");
        }
        adminService.increaseBookQuantity(bookId);
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if (admin == null || !admin.equals("admin")){
            throw new Exception("Administration page only");
        }
        adminService.decreaseBoookQuantity(bookId);
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if (admin == null || !admin.equals("admin")){
            throw new Exception("Administration page only");
        }
        adminService.deleteBook(bookId);

    }


}


