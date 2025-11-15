package hkmu.wadd.dao;


import hkmu.wadd.model.Lecture;
import hkmu.wadd.model.LectureNotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LectureNotesRepository extends JpaRepository<LectureNotes, UUID> {
    Optional<LectureNotes> findByIdAndLectureId(UUID id, Long lectureId);
}