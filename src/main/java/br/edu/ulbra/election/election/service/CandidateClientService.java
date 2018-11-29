package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateClientService {
    private final CandidateClient candidateClient;
    @Autowired
    public CandidateClientService(CandidateClient candidateClient){
        this.candidateClient = candidateClient;
    }
    public CandidateOutput getById(Long id){
        return this.candidateClient.getById(id);
    }
}
