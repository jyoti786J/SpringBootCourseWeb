package hkmu.wadd.controller;

import hkmu.wadd.dao.*;
import hkmu.wadd.dto.CommentTargetType;
import hkmu.wadd.exception.CommentNotFoundException;
import hkmu.wadd.exception.CourseNotFoundException;
import hkmu.wadd.model.Course;
import hkmu.wadd.model.Poll;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private PollService pollService;
    @Autowired
    private CommentService commentService;

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        Course course = courseService.getCourseView();
        model.addAttribute("course", course);
        model.addAttribute("lectures", course.getLectures());
        model.addAttribute("polls", course.getPolls());
        return "index";
    }

    @PostMapping("/comment")
    @PreAuthorize("isAuthenticated()")
    public String addCourseComment(@RequestParam String content,
                                   Principal principal) throws CourseNotFoundException {
        Course course = courseService.getCourse();
        commentService.addCourseComment(course.getId(), content, principal.getName());
        return "redirect:/course";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping("/{courseId}/comment/{commentId}/delete")
    public String deleteCourseComment(
            @PathVariable Long courseId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteCourseComment(commentId, userDetails.getUsername());
           // redirectAttributes.addFlashAttribute("message", "Course Comment deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid comment type");

        } catch (CommentNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/course/index";
    }
}