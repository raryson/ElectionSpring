package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import br.edu.ulbra.election.election.service.ResultService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/result")
public class ResultApi {

    private final ResultService resultService;

    public ResultApi(ResultService resultService){
        this.resultService = resultService;
    }

    @GetMapping("/election/{electionId}")
    public ResultOutput getResultByElection(@PathVariable Long electionId){
        ResultOutput result = new ResultOutput();
        result = resultService.getElectionResult(electionId);
        return result;
    }

    @GetMapping("/candidate/{candidateId}")
    public ElectionCandidateResultOutput getResultByCandidate(@PathVariable Long candidateId){
        return resultService.getElectionResultCandidate(candidateId);
    }

}
