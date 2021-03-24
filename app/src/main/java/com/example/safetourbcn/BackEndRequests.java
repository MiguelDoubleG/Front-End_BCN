package com.example.safetourbcn;

public interface BackEndRequests {
    public boolean addUser(String user, String password);
}



class userReq implements BackEndRequests {
    @Override
    public boolean addUser(String user, String password) {

        return false;
    }
}