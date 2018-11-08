package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ElectionService {

    private final ElectionRepository voterRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "election not found";

    @Autowired
    public ElectionService(ElectionRepository voterRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.voterRepository = voterRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ElectionOutput> getAll(){
        Type voterOutputListType = new TypeToken<List<ElectionOutput>>(){}.getType();
        return modelMapper.map(voterRepository.findAll(), voterOutputListType);
    }

    public ElectionOutput create(ElectionInput voterInput) {
        validateInput(voterInput, false);
        Election election = modelMapper.map(voterInput, Election.class);
        election = voterRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput getById(Long voterId){
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = voterRepository.findById(voterId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput getByYear(Integer year){
        if (year == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        List<Election> election = voterRepository.findByYear(year).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput update(Long voterId, ElectionInput voterInput) {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(voterInput, true);

        Election election = voterRepository.findById(voterId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        election.setDescription(voterInput.getDescription());
        election.setStateCode(voterInput.getStateCode());
        election.setYear(voterInput.getYear());
        election = voterRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public GenericOutput delete(Long voterId) {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = voterRepository.findById(voterId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        voterRepository.delete(election);

        return new GenericOutput("election deleted");
    }

    private void validateInput(ElectionInput voterInput, boolean isUpdate){
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

        if (StringUtils.isBlank(voterInput.getDescription())){
            throw new GenericOutputException("Invalid description");
        }
        if (StringUtils.isBlank(voterInput.getStateCode())) {
            throw new GenericOutputException("Invalid statecode");
        }

        if (voterInput.getYear() < 2000 || voterInput.getYear() > 2200) {
            throw new GenericOutputException("Invalid year");
        }

        if (voterInput.getDescription().length() < 5) {
            throw new GenericOutputException("Invalid description");
        }
        int counter = 0;
        for(String stateCode: states){
            if(stateCode ==  voterInput.getStateCode()){
                counter++;
            }
        }

        if (counter > 0){
            throw new GenericOutputException("State code is not br");
        }
    }

}
