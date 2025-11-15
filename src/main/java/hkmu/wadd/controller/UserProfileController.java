package hkmu.wadd.controller;

import hkmu.wadd.dao.*;
import hkmu.wadd.dto.CommentHistoryDTO;
import hkmu.wadd.dto.CommentTargetType;
import hkmu.wadd.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/view")
public class UserProfileController {
    private final UserService userService;
    private final CommentService commentService;
    private final PollVoteRepository voteRepository;
    private final PollCommentRepository pollCommentRepository;
    private final CourseCommentRepository courseCommentRepository;
    private final LectureCommentRepository lectureCommentRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserProfileController(UserService userService, CommentService commentService, PollVoteRepository voteRepository, PollCommentRepository pollCommentRepository, CourseCommentRepository courseCommentRepository, LectureCommentRepository lectureCommentRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.commentService = commentService;
        this.voteRepository = voteRepository;
        this.pollCommentRepository = pollCommentRepository;
        this.courseCommentRepository = courseCommentRepository;
        this.lectureCommentRepository = lectureCommentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public String viewProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/{username}/edit")
    public String editProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "editProfile";
    }

//    @PostMapping("/{username}/edit")
//    public String updateProfile(@PathVariable String username, User updatedUser) {
//        User existingUser = userService.findByUsername(username);
//        existingUser.setFullName(updatedUser.getFullName());
//        existingUser.setEmail(updatedUser.getEmail());
//        existingUser.setPhone(updatedUser.getPhone());
//        existingUser.setPhone(updatedUser.getPassword());
//        userService.updateUser(existingUser); // Implement this method in UserService
//        return "redirect:/view/" + username; // Redirect to the profile view after updating
//    }

    @PostMapping("/{username}/edit")
    public String updateProfile(@PathVariable String username,
                                @ModelAttribute("user") User updatedUser,
                                @RequestParam(required = false) String newPassword,
                                @RequestParam(required = false) String confirmPassword,
                                BindingResult result,
                                Model model) {

        User existingUser = userService.findByUsername(username);

        // Validate password if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                result.rejectValue("password", "error.user", "Passwords do not match");
            } else if (newPassword.length() < 6) {
                result.rejectValue("password", "error.user", "Password must be at least 6 characters");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("user", existingUser);
            return "editProfile";
        }

        // Update basic info
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());

        // Update password if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        userService.updateUser(existingUser);
        return "redirect:/view/" + username;
    }

    @GetMapping("/{username}/votes")
    public String viewVoteHistory(Principal principal, Model model) {
        String username = principal.getName();
        List<PollVote> userVotes = voteRepository.findByUsername(username);
        model.addAttribute("votes", userVotes);
        model.addAttribute("username", username);
        return "voteHistory";
    }

    @GetMapping("/{username}/comments")
    public String viewCommentHistory(@PathVariable String username, Model model) {
        // Get all three types of comments
        List<PollComment> pollComments = pollCommentRepository.findByAuthorUsernameOrderByCreatedAtDesc(username);
        List<CourseComment> courseComments = courseCommentRepository.findByAuthorUsernameOrderByCreatedAtDesc(username);
        List<LectureComment> lectureComments = lectureCommentRepository.findByAuthorUsernameOrderByCreatedAtDesc(username);

        // Combine and sort
        List<CommentHistoryDTO> allComments = new ArrayList<>();

        // Add poll comments
        for (PollComment pc : pollComments) {
            allComments.add(new CommentHistoryDTO(
                    pc.getId(),
                    pc.getContent(),
                    pc.getCreatedAt(),
                    "POLL",
                    pc.getPoll().getId(),
                    pc.getPoll().getQuestion()
            ));
        }

        // Add course comments
        for (CourseComment cc : courseComments) {
            allComments.add(new CommentHistoryDTO(
                    cc.getId(),
                    cc.getContent(),
                    cc.getCreatedAt(),
                    "COURSE",
                    cc.getCourse().getId(),
                    cc.getCourse().getCourseName()
            ));
        }

        // Add lecture comments
        for (LectureComment lc : lectureComments) {
            allComments.add(new CommentHistoryDTO(
                    lc.getId(),
                    lc.getContent(),
                    lc.getCreatedAt(),
                    "LECTURE",
                    lc.getLecture().getId(),
                    lc.getLecture().getLectureName()
            ));
        }

        // Sort by date (newest first)
        allComments.sort((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()));

        model.addAttribute("comments", allComments);
        model.addAttribute("username", username);
        return "commentHistory";
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{username}/comments/{commentId}/delete")
    public String deleteCommentFromHistory(
            @PathVariable String username,
            @PathVariable Long commentId,
            @RequestParam String targetType,
            RedirectAttributes redirectAttributes) {

        try {
            CommentTargetType type = CommentTargetType.fromString(targetType);
            commentService.deleteCommentFromHistory(commentId, type);
            redirectAttributes.addFlashAttribute("message", "Comment deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Delete failed: " + e.getMessage());
        }

        return "redirect:/view/" + username + "/comments";
    }
}
