package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.output.v1.VoterOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="login-service", url="http://localhost:8081")
public interface LoginClient {
    @GetMapping("/v1/login/check/{token}")
    VoterOutput checkToken(@PathVariable(name = "token") String token);


}
