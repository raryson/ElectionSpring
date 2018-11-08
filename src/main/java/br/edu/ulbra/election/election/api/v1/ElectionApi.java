package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.service.ElectionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/election")
public class ElectionApi {

    private final ElectionService _electionService;

    @Autowired
    public ElectionApi(ElectionService electionService){
        _electionService = electionService;
    }

    @GetMapping("/")
    @ApiOperation(value = "Get election List")
    public List<ElectionOutput> getAll(){
        return _electionService.getAll();
    }

    @GetMapping("/year/{year}")
    @ApiOperation(value = "Get election List by year")
    public List<ElectionOutput> getByYear(@PathVariable Integer year){
        return (List<ElectionOutput>) _electionService.getByYear(year);
    }

    @GetMapping("/{electionId}")
    @ApiOperation(value = "Get election by Id")
    public ElectionOutput getById(@PathVariable Long electionId){
        return _electionService.getById(electionId);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create new election")
    public ElectionOutput create(@RequestBody ElectionInput electionInput){
        return _electionService.create(electionInput);
    }

    @PutMapping(value = "Update election")
    public ElectionOutput update(@PathVariable Long electionId, @RequestBody ElectionInput electionInput){
        return _electionService.update(electionId, electionInput);
    }

    @DeleteMapping("/{electionId}")
    @ApiOperation(value = "Delete election")
    public GenericOutput delete(@PathVariable Long electionId){
        return _electionService.delete(electionId);
    }
}
