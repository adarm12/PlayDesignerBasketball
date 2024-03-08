package com.ashcollege.responses;

import com.ashcollege.entities.User;

import java.util.List;

public class ListUserResponse extends BasicResponse{

    private List<User> users;

    public ListUserResponse(boolean success, Integer errorCode, List<User> users) {
        super(success, errorCode);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
