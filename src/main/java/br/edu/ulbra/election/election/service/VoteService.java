package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.output.v1.VoterOutput;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "election not found";
    private static final String MESSAGE_CANDIDATE_NOT_FOUND = "candidate not found";
    private static final String MESSAGE_VOTER_NOT_FOUND = "voter not found";
    private final LoginClientService loginClientService;

    @Autowired
    public VoteService(VoteRepository voteRepository, ModelMapper modelMapper, LoginClientService loginClientService){
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
        this.loginClientService = loginClientService;
    }


    private void checkTokenAuth(String token)
    {
        try {
            VoterOutput loginData = loginClientService.checkToken(token);
        } catch (Exception ex) {
            throw new GenericOutputException("Invalid Token");
        }
    }

    public GenericOutput create(VoteInput voteInput, String token) {
        checkTokenAuth(token);
        validateInput(voteInput);
        Vote vote = new Vote();
        vote.setElectionId(voteInput.getElectionId());
        vote.setCandidateId(voteInput.getCandidateId());
        vote.setVoterId(voteInput.getVoterId());
        try {
            voteRepository.save(vote);
            return new GenericOutput("OK");
        } catch(Exception ex) {
            throw new GenericOutputException("Error on vote");
        }
    }

    public GenericOutput createAll(List<VoteInput> voteInput, String token) {
        checkTokenAuth(token);
        for(VoteInput voted: voteInput){
            validateInput(voted);
            Vote vote = new Vote();
            vote.setElectionId(voted.getElectionId());
            vote.setCandidateId(voted.getCandidateId());
            vote.setVoterId(voted.getVoterId());
            try {
                vote = voteRepository.save(vote);
            } catch(Exception ex) {
                throw new GenericOutputException("Error on vote");
            }
        }
        return new GenericOutput("Ok");

    }

    private void validateInput(VoteInput voteInput){
        Vote electionHasVotes = voteRepository.findFirstByElectionId(voteInput.getElectionId()).orElse(null);
        Vote voterHasVotes = voteRepository.findFirstByVoterId(voteInput.getVoterId()).orElse(null);
        if(electionHasVotes != null && voterHasVotes != null){
            throw new GenericOutputException("Cannot vote on same election");
        }
    }

}
