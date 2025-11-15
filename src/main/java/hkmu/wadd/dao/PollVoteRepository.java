package hkmu.wadd.dao;

import hkmu.wadd.model.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
    Optional<PollVote> findByPollIdAndUsername(Long pollId, String username);
    int countBySelectedOptionId(Long optionId);

    @Query("SELECT COUNT(v) FROM PollVote v WHERE v.selectedOption.id = :optionId")
    int countVotesByOptionId(@Param("optionId") Long optionId);

    void deleteByPollId(Long pollId);

    void deleteByUsername(String username);

    List<PollVote> findByUsername(String username);
}