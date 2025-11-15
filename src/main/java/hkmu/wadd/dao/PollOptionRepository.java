package hkmu.wadd.dao;

import hkmu.wadd.model.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    void deleteByPollId(Long pollId);

    Set<PollOption> findByPollId(Long pollId);
}
