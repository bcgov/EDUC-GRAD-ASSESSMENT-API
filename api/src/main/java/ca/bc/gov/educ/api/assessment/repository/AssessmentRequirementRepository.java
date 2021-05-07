package ca.bc.gov.educ.api.assessment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;

@Repository
public interface AssessmentRequirementRepository extends JpaRepository<AssessmentRequirementEntity, UUID> {

    List<AssessmentRequirementEntity> findAll();

	Page<AssessmentRequirementEntity> findByRuleCode(String rule, Pageable paging);

	List<AssessmentRequirementEntity> findByAssessmentCodeIn(List<String> assessmentCodes);

}
