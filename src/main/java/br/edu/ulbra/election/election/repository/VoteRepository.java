package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Optional<Vote> findFirstByElectionId(Long electionid);
    Optional<Vote> findFirstByVoterId(Long voterId);
    Optional<List<Vote>> findAllByElectionId(Long voterId);
    Optional<List<Vote>> findAllByCandidateId(Long candidateId);
    Optional<List<Vote>> findAllByCandidateIdAndElectionId(Long candidateId, Long ElectionId);
}
