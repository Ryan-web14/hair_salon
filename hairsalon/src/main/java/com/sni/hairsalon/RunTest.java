package com.sni.hairsalon;

import org.springframework.beans.factory.annotation.Autowired;

import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.repository.ClientRepository;


public class RunTest {

    @Autowired
    private ClientRepository clientRepository;


    public void retrieveUser(){
       

			long id = 309795119838522536L;

            try{

                Client foundClient = clientRepository.findById(id).orElseThrow();
                String clientString = foundClient.toString();

            System.out.println(clientString);
            }catch(RuntimeException e){
                e.printStackTrace();
            }
	
			
    }
}

