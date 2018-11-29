package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1/vote")
public class VoteApi {

    private final VoteService voteService;

    @Autowired
    public VoteApi(VoteService voteService){
        this.voteService = voteService;
    }

    @PostMapping("/")
    public GenericOutput electionVote(@RequestHeader(value = "x-token") String token, @RequestBody VoteInput voteInput){
        return voteService.create(voteInput);
    }

    @PostMapping("/multiple")
    public GenericOutput multipleElectionVote(@RequestHeader(value = "x-token") String token, @RequestBody List<VoteInput> voteInputList){
        return voteService.createAll(voteInputList);
    }

}
