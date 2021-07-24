package ca.bc.gov.educ.api.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementCodeEntity;

@Repository
public interface AssessmentRequirementCodeRepository extends JpaRepository<AssessmentRequirementCodeEntity, String> {

}
