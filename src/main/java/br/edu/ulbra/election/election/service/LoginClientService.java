package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.output.v1.VoterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginClientService {
    private final LoginClient loginClient;
    @Autowired
    public LoginClientService(LoginClient loginClient){
        this.loginClient = loginClient;
    }
    public VoterOutput checkToken(String token){
        return this.loginClient.checkToken(token);
    }
}
