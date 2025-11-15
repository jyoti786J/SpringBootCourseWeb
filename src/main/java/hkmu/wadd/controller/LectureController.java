package hkmu.wadd.controller;

import hkmu.wadd.dao.CommentService;
import hkmu.wadd.dao.CourseRepository;
import hkmu.wadd.dao.LectureService;
import hkmu.wadd.dto.CommentTargetType;
import hkmu.wadd.exception.CourseNotFoundException;
import hkmu.wadd.exception.LectureNotFoundException;
import hkmu.wadd.exception.LectureNoteNotFoundException;
import hkmu.wadd.model.Course;
import hkmu.wadd.model.Lecture;
import hkmu.wadd.model.LectureComment;
import hkmu.wadd.model.LectureNotes;
import hkmu.wadd.view.DownloadingView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import hkmu.wadd.dto.CommentForm;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/course/{courseId}/lecture")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/{lectureId}")
    public String viewLecture(@PathVariable Long courseId,
                              @PathVariable Long lectureId,
                              Model model,
                              Principal principal) throws LectureNotFoundException, CourseNotFoundException {
        Lecture lecture = lectureService.getLectureWithNotes(courseId,lectureId);
        List<LectureComment> comments = commentService.getLectureComments(lectureId);

        List<LectureNotes> notes = lecture.getLectureNotes();

        model.addAttribute("lecture", lecture);
        model.addAttribute("comments", comments);
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("courseId", courseId);
        model.addAttribute("notes", notes);

        return "viewLecture";
    }

    @PostMapping("/{lectureId}/comment")
    @PreAuthorize("isAuthenticated()")
    public String addLectureComment(@PathVariable Long courseId,
                                    @PathVariable Long lectureId,
                                    @Valid @ModelAttribute CommentForm commentForm,
                                    BindingResult result,
                                    Principal principal) throws LectureNotFoundException {
        if (result.hasErrors()) {
            return "viewLecture";
        }

        commentService.addLectureComment(lectureId, commentForm.getContent(), principal.getName());
        return "redirect:/course/" + courseId + "/lecture/" + lectureId;
    }



    @GetMapping("/{lectureId}/note/add")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String showAddNoteForm(@PathVariable Long courseId,
                                  @PathVariable Long lectureId,
                                  Model model) throws CourseNotFoundException, LectureNotFoundException {
        Lecture lecture = lectureService.getLectureWithNotes(courseId, lectureId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        model.addAttribute("course", course);
        model.addAttribute("lecture", lecture);
        return "addLectureNote";
    }

    @PostMapping("/{lectureId}/note/add")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String addLectureNote(@PathVariable Long courseId,
                                 @PathVariable Long lectureId,
                                 @RequestParam("name") String displayName,
                                 @RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal)
            throws IOException, CourseNotFoundException, LectureNotFoundException {

        try {
            // Validate file
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "File cannot be empty");
                return "redirect:/course/" + courseId + "/lecture/" + lectureId + "/addNote";
            }

            // Validate file size (10MB max)
            if (file.getSize() > 10 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("errorMessage", "File size exceeds 10MB limit");
                return "redirect:/course/" + courseId + "/lecture/" + lectureId + "/addNote";
            }

            // Validate display name
            if (displayName == null || displayName.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Display name is required");
                return "redirect:/course/" + courseId + "/lecture/" + lectureId + "/addNote";
            }

            // Get the lecture
            Lecture lecture = lectureService.getLectureWithNotes(courseId, lectureId);

            // Create new lecture note
            LectureNotes note = new LectureNotes();
            note.setName(displayName.trim());
            note.setMimeContentType(file.getContentType());
            note.setContents(file.getBytes());
            note.setUploadedBy(principal.getName());
            note.setUploadedAt(LocalDateTime.now());
            note.setLecture(lecture);

            // Add note to lecture
            lecture.getLectureNotes().add(note);

            // Save
            lectureService.addLectureNote(lecture);

            redirectAttributes.addFlashAttribute("successMessage", "Note added successfully");
            return "redirect:/course/" + courseId + "/lecture/" + lectureId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding note: " + e.getMessage());
            return "redirect:/course/" + courseId + "/lecture/" + lectureId + "/addNote";
        }
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String showAddLectureForm(@PathVariable Long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("lectureForm", new LectureForm());
        return "addLecture";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String addLecture(@PathVariable Long courseId,
                             @Valid @ModelAttribute LectureForm form,
                             @RequestParam("name") String displayName,
                             BindingResult result,
                             Principal principal) throws CourseNotFoundException, IOException {
        if (result.hasErrors()) {
            return "addLecture";
        }
        lectureService.createLecture(courseId, displayName, form, principal.getName());
        return "redirect:/course/index";
    }

    @GetMapping("/{lectureId}/note/{noteId}")
    public View downloadNote(@PathVariable Long lectureId,
                             @PathVariable UUID noteId) throws LectureNoteNotFoundException, LectureNotFoundException {
        LectureNotes note = lectureService.getLectureNote(lectureId, noteId);
        return new DownloadingView(note.getName(), note.getMimeContentType(), note.getContents());
    }

    @PostMapping("/{lectureId}/delete")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String deleteLecture(@PathVariable Long courseId,
                                @PathVariable Long lectureId) throws LectureNotFoundException {
        lectureService.deleteLecture(lectureId);
        return "redirect:/course/index";
    }

    @GetMapping("/{lectureId}/edit")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String showEditLectureForm(@PathVariable Long courseId,
                                      @PathVariable Long lectureId,
                                      Model model) throws CourseNotFoundException, LectureNotFoundException {
        Lecture lecture = lectureService.getLectureWithNotes(courseId, lectureId);

        LectureForm form = new LectureForm();
        form.setLectureNo(lecture.getLectureNo());
        form.setLectureName(lecture.getLectureName());

        model.addAttribute("courseId", courseId);
        model.addAttribute("lectureId", lectureId);
        model.addAttribute("lecture", lecture); // Add lecture object to access notes
        model.addAttribute("lectureForm", form);
        return "editLecture";
    }


    @PostMapping("/{lectureId}/edit")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String editLecture(@PathVariable Long courseId,
                              @PathVariable Long lectureId,
                              @Valid @ModelAttribute LectureForm form,
                              BindingResult result,
                              Principal principal,
                              RedirectAttributes redirectAttributes)
            throws CourseNotFoundException, LectureNotFoundException, IOException {

        if (result.hasErrors()) {
            return "editLecture";
        }

        try {
            lectureService.updateLecture(courseId, lectureId, form, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Lecture updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update lecture");
            //logger.error("Error updating lecture", e);
        }

        return "redirect:/course/" + courseId + "/lecture/" + lectureId;
    }

    @PostMapping("/{lectureId}/note/{noteId}/delete")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public String deleteNote(@PathVariable Long courseId,
                             @PathVariable Long lectureId,
                             @PathVariable UUID noteId,
                             RedirectAttributes redirectAttributes) {

        try {
            // Verify lecture exists and belongs to course
            Lecture lecture = lectureService.getLectureWithNotes(courseId, lectureId);

            // Delete the note
            lectureService.deleteNote(lectureId, noteId);

            // Add success message
            redirectAttributes.addFlashAttribute("success", "lecture.note.delete.success");

        } catch (LectureNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "lecture.not.found");
        } catch (LectureNoteNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "lecture.note.not.found");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "lecture.note.delete.error");
        }

        return "redirect:/course/" + courseId + "/lecture/" + lectureId;
    }

    @PostMapping("/{lectureId}/comment/{commentId}/delete")
    public String deleteLectureComment(
            @PathVariable Long courseId,
            @PathVariable Long lectureId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        try {
            commentService.deleteLectureComment(commentId, userDetails.getUsername());
           // redirectAttributes.addFlashAttribute("message", "Comment deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/course/" + courseId + "/lecture/" + lectureId;
    }

    public static class LectureForm {
        @NotNull @Min(1)
        private Integer lectureNo;

        @NotBlank @Size(max=100)
        private String lectureName;

        private List<MultipartFile> newLectureNotes;  // Changed from lectureNotes to newLectureNotes
        private List<LectureNotes> existingNotes;     // Add this for existing notes

        // Getters and setters
        public Integer getLectureNo() { return lectureNo; }
        public void setLectureNo(Integer lectureNo) { this.lectureNo = lectureNo; }
        public String getLectureName() { return lectureName; }
        public void setLectureName(String lectureName) { this.lectureName = lectureName; }
        public List<MultipartFile> getNewLectureNotes() { return newLectureNotes; }
        public void setNewLectureNotes(List<MultipartFile> newLectureNotes) { this.newLectureNotes = newLectureNotes; }
        public List<LectureNotes> getExistingNotes() { return existingNotes; }
        public void setExistingNotes(List<LectureNotes> existingNotes) { this.existingNotes = existingNotes; }
    }
}