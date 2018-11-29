package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.*;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {

    private final VoteRepository voteRepository;
    private final CandidateClientService candidateClientService;

    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "election not found";
    private static final String MESSAGE_CANDIDATE_NOT_FOUND = "candidate not found";
    private static final String MESSAGE_VOTER_NOT_FOUND = "voter not found";
    private final ElectionService electionService;

    @Autowired
    public ResultService(VoteRepository voteRepository, ModelMapper modelMapper,
                         CandidateClientService candidateClientService, ElectionService electionService){
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
        this.candidateClientService = candidateClientService;
        this.electionService = electionService;
    }

    public ElectionCandidateResultOutput getElectionResultCandidate(Long candidateId){
        ElectionCandidateResultOutput result = new ElectionCandidateResultOutput();
        CandidateOutput candidate = candidateClientService.getById(candidateId);
        List<Vote> allCandidateVotes = voteRepository.findAllByCandidateId(candidateId).orElse(null);
        result.setCandidate(candidate);
        result.setTotalVotes((long) allCandidateVotes.size());
        return result;
    }

    public ResultOutput getElectionResult(Long electionId){
        List<Vote> votesByElection = voteRepository.findAllByElectionId(electionId).orElse(null);
        if (votesByElection == null)
            throw new GenericOutputException("Invalid Election Id");
        long numberAllVotes = votesByElection.size();
        long blankVotes = 0;
        long nullVotes = 0;
        ResultOutput result = new ResultOutput();
        ArrayList<ElectionCandidateResultOutput> resultCandidate = new ArrayList<>();
        for(Vote voted : votesByElection){
            if(voted.getCandidateId() == null) {
                blankVotes++;
            } else {
                List<Vote> electionVotes = voteRepository.findAllByCandidateIdAndElectionId(voted.getCandidateId(), voted.getElectionId()).orElse(null);
                if(electionVotes == null) {
                    nullVotes++;
                } else {
                    ElectionCandidateResultOutput candidate = new ElectionCandidateResultOutput();
                    candidate.setCandidate(candidateClientService.getById(voted.getCandidateId()));
                    candidate.setTotalVotes((long)electionVotes.size());
                    resultCandidate.add(candidate);
                }

            }

        }

        result.setCandidates(resultCandidate);
        result.setBlankVotes(blankVotes);
        result.setTotalVotes(numberAllVotes);
        result.setElection(electionService.getById(electionId));
        return result;
    }



    private void validateInput(VoteInput voteInput){

    }

}
