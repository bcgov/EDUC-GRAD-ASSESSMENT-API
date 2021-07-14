package ca.bc.gov.educ.api.assessment.repository;

import ca.bc.gov.educ.api.assessment.model.entity.StudentAssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentAssessmentRepository extends JpaRepository<StudentAssessmentEntity, UUID> {

	@Query("select c from StudentAssessmentEntity c where c.assessmentKey.pen=:pen")
    Iterable<StudentAssessmentEntity> findByPen(String pen);

}
