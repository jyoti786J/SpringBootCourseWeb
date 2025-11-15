package hkmu.wadd.dao;

import hkmu.wadd.exception.CourseNotFoundException;
import hkmu.wadd.exception.PollNotFoundException;
import hkmu.wadd.exception.PollOptionNotFoundException;
import hkmu.wadd.exception.UnauthorizedException;
import hkmu.wadd.model.*;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PollService {
    private final PollRepository pollRepository;
    private final PollOptionRepository optionRepository;
    private final PollVoteRepository voteRepository;
    private final PollCommentRepository commentRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Autowired
    public PollService(PollRepository pollRepository,
                       PollOptionRepository optionRepository,
                       PollVoteRepository voteRepository,
                       PollCommentRepository commentRepository,
                       CourseRepository courseRepository,
                       UserService userService) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.userService = userService;
    }


    public void createPoll(Long courseId, String question, List<String> options, String createdBy) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        Poll poll = new Poll();
        poll.setQuestion(question);
        poll.setCourse(course);
        poll.setCreatedBy(createdBy);

        // Create exactly 4 options as required
        for (String optionText : options) {
            PollOption option = new PollOption();
            option.setText(optionText);
            option.setPoll(poll);
            poll.getOptions().add(option);
        }

        pollRepository.save(poll);
    }

    public Poll getPollWithVotesAndComments(Long pollId) throws PollNotFoundException {
        Poll poll = pollRepository.findWithOptionsById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        // Initialize comments and options
        Hibernate.initialize(poll.getComments());
        Hibernate.initialize(poll.getOptions());

        // Calculate vote counts if not already done
        if (poll.getOptions() != null) {
            for (PollOption option : poll.getOptions()) {
                option.setVoteCount(voteRepository.countBySelectedOptionId(option.getId()));
            }
        }

        return poll;
    }


    public PollVote getUserVote(Long pollId, String username) {
        return voteRepository.findByPollIdAndUsername(pollId, username).orElse(null);
    }

    @Transactional
    public void vote(Long pollId, Long optionId, String username) throws PollNotFoundException {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        PollOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new PollOptionNotFoundException(optionId));

        Optional<PollVote> existingVote = voteRepository.findByPollIdAndUsername(pollId, username);

        if (existingVote.isPresent()) {
            // Update existing vote - decrement old option, increment new option
            PollVote vote = existingVote.get();
            if (!vote.getSelectedOption().getId().equals(optionId)) {
                vote.getSelectedOption().decrementVoteCount(); // Decrement old option
                option.incrementVoteCount(); // Increment new option
            }
            vote.setSelectedOption(option);
        } else {
            // Create new vote - increment the option count
            PollVote vote = new PollVote();
            vote.setPoll(poll);
            vote.setUsername(username);
            vote.setSelectedOption(option);
            voteRepository.save(vote);
            option.incrementVoteCount(); // Increment the option count
        }

        // Save the updated option
        optionRepository.save(option);
    }


    public PollComment addComment(Long pollId, String content, String username) throws PollNotFoundException {
        User user = userService.findByUsername(username);
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        PollComment comment = new PollComment();
        comment.setContent(content);
        comment.setAuthor(user);
        comment.setPoll(poll);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Poll> getPollsByCourse(Long courseId) {
        return pollRepository.findByCourseId(courseId);
    }

    public void deletePoll(Long pollId) throws PollNotFoundException {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        // First delete all votes associated with the poll's options
        voteRepository.deleteByPollId(pollId);

        // Then delete all comments associated with the poll
        commentRepository.deleteByPollId(pollId);

        // Then delete all options
        optionRepository.deleteByPollId(pollId);

        // Finally delete the poll itself
        pollRepository.delete(poll);
    }

    @Transactional
    public void updatePoll(Long pollId, String question, List<String> options, String updatedBy)
            throws PollNotFoundException {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        poll.setQuestion(question);
        //poll.setUpdatedBy(updatedBy);
        //poll.setUpdatedAt(LocalDateTime.now());

        // Update existing options or create new ones
        Set<PollOption> existingOptions = optionRepository.findByPollId(pollId);
        List<PollOption> existingOptionsList = new ArrayList<>(existingOptions);

        for (int i = 0; i < options.size(); i++) {
            String optionText = options.get(i);
            PollOption option;

            if (i < existingOptionsList.size()) {
                // Update existing option
                option = existingOptionsList.get(i);
                option.setText(optionText);
            } else {
                // Create new option
                option = new PollOption();
                option.setText(optionText);
                option.setPoll(poll);
                poll.getOptions().add(option);
            }

            optionRepository.save(option);
        }

        // Delete any extra options if they existed before
        if (existingOptions.size() > options.size()) {
            for (int i = options.size(); i < existingOptionsList.size(); i++) {
                PollOption extraOption = existingOptionsList.get(i);
                optionRepository.delete(extraOption);
                poll.getOptions().remove(extraOption);
            }
        }

        pollRepository.save(poll);
    }

    public Poll getPollWithOptions(Long pollId) throws PollNotFoundException {
        return pollRepository.findWithOptionsById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));
    }

}