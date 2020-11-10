package ca.bc.gov.educ.api.assessment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;

@Repository
public interface AssessmentRepository extends JpaRepository<AssessmentEntity, String> {

    List<AssessmentEntity> findAll();

	Optional<AssessmentEntity> findByAssessmentCode(String assmtCode);

}
