package com.example.assignment_2_ex_1;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersRESTAPI {
    @GET("users/")
    Call<Users> getUsers();


}
