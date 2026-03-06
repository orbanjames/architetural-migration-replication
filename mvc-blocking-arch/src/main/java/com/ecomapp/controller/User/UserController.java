package com.ecomapp.controller.User;

import com.ecomapp.models.User;
import com.ecomapp.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getByID(userId);
    }

    @GetMapping("/{userId}/payment/{paymentId}/status")
    public String getCustomerPaymentStatus(@PathVariable int userId,
                                           @PathVariable int paymentId) {
        return userService.getUserPaymentStatus(paymentId, userId);
    }

    @GetMapping("/{userId}/synods/{synodId}/status")
    public String getUserSynodStatus(@PathVariable int userId,
                                     @PathVariable int synodId) {
        return userService.getUserSynodStatus(synodId, userId);
    }
}