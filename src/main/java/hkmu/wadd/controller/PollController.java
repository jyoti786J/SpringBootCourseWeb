package hkmu.wadd.controller;

import hkmu.wadd.dao.*;
import hkmu.wadd.dto.CommentForm;
import hkmu.wadd.dto.CommentTargetType;
import hkmu.wadd.exception.CommentNotFoundException;
import hkmu.wadd.exception.CourseNotFoundException;
import hkmu.wadd.exception.PollNotFoundException;
import hkmu.wadd.exception.UnauthorizedException;
import hkmu.wadd.model.Course;
import hkmu.wadd.model.Poll;
import hkmu.wadd.model.PollOption;
import hkmu.wadd.model.PollVote;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/course/{courseId}/poll")
public class PollController {
    private final PollService pollService;
    private final CommentService commentService;

    @Autowired
    public PollController(PollService pollService, CommentService commentService) {
        this.pollService = pollService;
        this.commentService = commentService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String showCreatePollForm(@PathVariable Long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("pollForm", new PollForm());
        return "createPoll";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String createPoll(@PathVariable Long courseId,
                             @Valid @ModelAttribute PollForm pollForm,
                             BindingResult result,
                             Principal principal) throws CourseNotFoundException {
        if (result.hasErrors()) {
            return "createPoll";
        }

        List<String> options = Arrays.asList(
                pollForm.getOption1(),
                pollForm.getOption2(),
                pollForm.getOption3(),
                pollForm.getOption4()
        );

        pollService.createPoll(courseId, pollForm.getQuestion(), options, principal.getName());
        return "redirect:/course/index";
    }

    @GetMapping("/{pollId}")
    public String viewPoll(@PathVariable Long courseId,
                           @PathVariable Long pollId,
                           Model model,
                           Principal principal) throws PollNotFoundException {
        Poll poll = pollService.getPollWithVotesAndComments(pollId);
        PollVote userVote = principal != null ? pollService.getUserVote(pollId, principal.getName()) : null;

        model.addAttribute("poll", poll);
        model.addAttribute("courseId", courseId);
        model.addAttribute("userVote", userVote);
        model.addAttribute("commentForm", new CommentForm());
        return "viewPoll";
    }

    @PostMapping("/{pollId}/vote")
    @PreAuthorize("isAuthenticated()")
    public String vote(@PathVariable Long courseId,
                       @PathVariable Long pollId,
                       @RequestParam Long optionId,
                       Principal principal) throws PollNotFoundException {
        pollService.vote(pollId, optionId, principal.getName());
        return "redirect:/course/" + courseId + "/poll/" + pollId;
    }

    @PostMapping("/{pollId}/comment")
    @PreAuthorize("isAuthenticated()")
    public String addPollComment(@PathVariable Long courseId,
                                 @PathVariable Long pollId,
                                 @RequestParam String content,
                                 Principal principal) throws PollNotFoundException {
        commentService.addPollComment(pollId, content, principal.getName());
        return "redirect:/course/" + courseId + "/poll/" + pollId;
    }

    @PostMapping("/{pollId}/delete")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String deletePoll(@PathVariable Long courseId,
                             @PathVariable Long pollId,
                             RedirectAttributes redirectAttributes) {
        try {
            pollService.deletePoll(pollId);
            redirectAttributes.addFlashAttribute("success", "Poll deleted successfully");
        } catch (PollNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Poll not found");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting poll");
        }
        return "redirect:/course/index" ;
    }

    @GetMapping("/{pollId}/edit")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String showEditPollForm(@PathVariable Long courseId,
                                   @PathVariable Long pollId,
                                   Model model) throws PollNotFoundException {
        Poll poll = pollService.getPollWithOptions(pollId);

        PollForm pollForm = new PollForm();
        pollForm.setQuestion(poll.getQuestion());

        // Set options if they exist
        Set<PollOption> options = poll.getOptions();
        if (options != null && !options.isEmpty()) {
            List<PollOption> optionsList = new ArrayList<>(options);

            for (int i = 0; i < 4; i++) {
                if (i < optionsList.size()) {
                    switch (i) {
                        case 0:
                            pollForm.setOption1(optionsList.get(i).getText());
                            break;
                        case 1:
                            pollForm.setOption2(optionsList.get(i).getText());
                            break;
                        case 2:
                            pollForm.setOption3(optionsList.get(i).getText());
                            break;
                        case 3:
                            pollForm.setOption4(optionsList.get(i).getText());
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (i) {
                        case 0:
                            pollForm.setOption1("");
                            break;
                        case 1:
                            pollForm.setOption2("");
                            break;
                        case 2:
                            pollForm.setOption3("");
                            break;
                        case 3:
                            pollForm.setOption4("");
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        model.addAttribute("courseId", courseId);
        model.addAttribute("pollId", pollId);
        model.addAttribute("pollForm", pollForm);
        return "editPoll";
    }

    @PostMapping("/{pollId}/edit")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String editPoll(@PathVariable Long courseId,
                           @PathVariable Long pollId,
                           @Valid @ModelAttribute PollForm pollForm,
                           BindingResult result,
                           Principal principal) throws PollNotFoundException, CourseNotFoundException {
        if (result.hasErrors()) {
            return "editPoll";
        }

        List<String> options = Arrays.asList(
                pollForm.getOption1(),
                pollForm.getOption2(),
                pollForm.getOption3(),
                pollForm.getOption4()
        );

        pollService.updatePoll(pollId, pollForm.getQuestion(), options, principal.getName());
        return "redirect:/course/" + courseId + "/poll/" + pollId;
    }
    public static class PollForm {
        @NotBlank private String question;
        @NotBlank private String option1;
        @NotBlank private String option2;
        @NotBlank private String option3;
        @NotBlank private String option4;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getOption1() {
            return option1;
        }

        public void setOption1(String option1) {
            this.option1 = option1;
        }

        public String getOption2() {
            return option2;
        }

        public void setOption2(String option2) {
            this.option2 = option2;
        }

        public String getOption3() {
            return option3;
        }

        public void setOption3(String option3) {
            this.option3 = option3;
        }

        public String getOption4() {
            return option4;
        }

        public void setOption4(String option4) {
            this.option4 = option4;
        }
    }
    @PostMapping("/{pollId}/comment/{commentId}/delete")
    public String deletePollComment(
            @PathVariable Long courseId,
            @PathVariable Long pollId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) throws CommentNotFoundException {

        commentService.deletePollComment(commentId, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("message", "Poll comment deleted successfully");
        return "redirect:/course/" + courseId + "/poll/" + pollId;
    }
}