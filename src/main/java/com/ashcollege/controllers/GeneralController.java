package com.ashcollege.controllers;

import com.ashcollege.Persist;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ashcollege.utils.Errors.*;

@RestController
public class GeneralController {

    @Autowired
    private DbUtils dbUtils;

    @Autowired
    private Persist persist;


    @RequestMapping(value = "add-user", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse addUser(String username, String password, String repeatPassword) {
        return persist.insertUser(username, password, repeatPassword);
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public LoginResponse login(String username, String password) {
        return persist.login(username, password);
    }

    @RequestMapping(value = "search-user", method = {RequestMethod.GET, RequestMethod.POST})
    public List<User> searchUser(String secretFrom, String partOfUsername) {
        return persist.searchUser(secretFrom, partOfUsername);
    }

    @RequestMapping(value = "friend-request", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse friendRequest(String secretFrom, String usernameTo) {
        return persist.friendRequest(secretFrom, usernameTo);
    }

    @RequestMapping(value = "accept-friend", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse acceptFriend(String secret, String usernameTo) {
        return persist.acceptFriend(secret, usernameTo);
    }

    @RequestMapping(value = "show-requesters", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse showRequesters(String secretFrom) {
        return persist.showRequestersList(secretFrom);
    }

    @RequestMapping(value = "create-play", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse createPlay(String userSecret, String playName) {
        return persist.createPlay(userSecret,playName);
    }


    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test() {
        return "Hello From Server";
    }


}
