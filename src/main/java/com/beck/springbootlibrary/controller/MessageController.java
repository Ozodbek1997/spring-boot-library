package com.beck.springbootlibrary.controller;


import com.beck.springbootlibrary.entity.Message;
import com.beck.springbootlibrary.requestmodels.AdminQuestionRequest;
import com.beck.springbootlibrary.service.MessagesService;
import com.beck.springbootlibrary.utils.ExtractJWT;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("https://localhost:3000")
@RequestMapping("api/messages")
public class MessageController {

    private final MessagesService messagesService;

    public MessageController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token, @RequestBody Message messageRequest) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messagesService.postMessage(messageRequest, userEmail);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization") String token,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if (admin == null || !admin.equals("admin")){
            throw new Exception("Adminstration page only");
        }
        messagesService.putMessage(adminQuestionRequest,userEmail);
    }

}
