package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;

    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "election not found";
    private final VoteRepository voteRepository;

    @Autowired
    public ElectionService(ElectionRepository electionRepository, ModelMapper modelMapper, 
                           VoteRepository voteRepository){
        this.electionRepository = electionRepository;
        this.modelMapper = modelMapper;
        this.voteRepository = voteRepository;
    }

    public List<ElectionOutput> getAll(){
        Type voterOutputListType = new TypeToken<List<ElectionOutput>>(){}.getType();
        return modelMapper.map(electionRepository.findAll(), voterOutputListType);
    }

    public ElectionOutput create(ElectionInput electionInput) {
        validateInput(electionInput, false);
        Election election = modelMapper.map(electionInput, Election.class);
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput getById(Long electionId){
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput getByYear(Integer year){
        if (year == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        List<Election> election = electionRepository.findByYear(year).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput update(Long electionId, ElectionInput electionInput) {
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(electionInput, true);

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        checkIfHasVotesOnElection(electionId);
        checkIfElectionHasCandidates(electionId);

        election.setDescription(electionInput.getDescription());
        election.setStateCode(electionInput.getStateCode());
        election.setYear(electionInput.getYear());
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public GenericOutput delete(Long electionId) {
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }
        checkIfHasVotesOnElection(electionId);
        checkIfElectionHasCandidates(electionId);

        electionRepository.delete(election);

        return new GenericOutput("election deleted");
    }

    private void checkIfHasVotesOnElection(Long electionId){
        Vote vote = voteRepository.findFirstByElectionId(electionId).orElse(null);
        if(vote != null){
            throw new GenericOutputException("cannot update or delete election with votes");
        }
    }

    private void checkIfElectionHasCandidates(Long electionId){
        List<Vote> votes = voteRepository.findAllByElectionId(electionId).orElse(null);
        if (votes != null){
            for(Vote voted : votes){
                if(voted.getCandidateId() != null || voted.getCandidateId().toString().isEmpty()){
                    throw new GenericOutputException("cannot update or delete election with candidates");
                }
            }
        }
    }

    private void validateInput(ElectionInput electionInput, boolean isUpdate){
        String[] states =
                {"AC",
                        "AL",
                        "AP",
                        "AM",
                        "BA",
                        "CE",
                        "DF",
                        "ES",
                        "GO",
                        "MA",
                        "MT",
                        "MS",
                        "MG",
                        "PA",
                        "PB",
                        "PR",
                        "PE",
                        "PI",
                        "RJ",
                        "RN",
                        "RS",
                        "RO",
                        "RR",
                        "SC",
                        "SP",
                        "SE",
                        "TO" };

        if (StringUtils.isBlank(electionInput.getDescription())){
            throw new GenericOutputException("Invalid description");
        }
        if (StringUtils.isBlank(electionInput.getStateCode())) {
            throw new GenericOutputException("Invalid statecode");
        }

        if (electionInput.getYear() < 2000 || electionInput.getYear() > 2200) {
            throw new GenericOutputException("Invalid year");
        }

        if (electionInput.getDescription().length() < 5) {
            throw new GenericOutputException("Invalid description");
        }
        int counter = 0;
        for(String stateCode: states){
            if(stateCode !=  electionInput.getStateCode()){
                counter++;
            }
        }

        if (counter == 0){
            throw new GenericOutputException("State code is not br");
        }
    }

}
