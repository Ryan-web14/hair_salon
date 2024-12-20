package com.sni.hairsalon.dto.request;

import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
public class ClientRequestDTO {
    private User user;
    private String firstname;
    private String lastname;
    private int phone;
    private int noShowCount;

    public Client toEntity(Client clientRequest){
        Client client = new Client();
        client.setUser(user);
        client.setFirstname(firstname);
        client.setLastname(lastname);

        String verifyingPhone = Integer.toString(client.getPhoneNumber()); 
        return client;

    }

}
