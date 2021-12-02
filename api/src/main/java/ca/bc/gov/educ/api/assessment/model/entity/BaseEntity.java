package ca.bc.gov.educ.api.assessment.model.entity;

import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity {
	@Column(name = "CREATE_USER", nullable = true)
    private String createdBy;
	
	@Column(name = "CREATE_DATE", nullable = true)
    private Date createdTimestamp;
	
	@Column(name = "UPDATE_USER", nullable = true)
    private String updatedBy;
	
	@Column(name = "UPDATE_DATE", nullable = true)
    private Date updatedTimestamp;
	
	@PrePersist
	protected void onCreate() {
		if (StringUtils.isBlank(createdBy)) {
			this.createdBy = EducAssessmentApiConstants.DEFAULT_CREATED_BY;
		}		
		if (StringUtils.isBlank(updatedBy)) {
			this.updatedBy = EducAssessmentApiConstants.DEFAULT_UPDATED_BY;
		}		
		this.createdTimestamp = new Date(System.currentTimeMillis());
		this.updatedTimestamp = new Date(System.currentTimeMillis());

	}

	@PreUpdate
	protected void onPersist() {
		this.updatedTimestamp = new Date(System.currentTimeMillis());
		if (StringUtils.isBlank(updatedBy)) {
			this.updatedBy = EducAssessmentApiConstants.DEFAULT_UPDATED_BY;
		}
		if (StringUtils.isBlank(createdBy)) {
			this.createdBy = EducAssessmentApiConstants.DEFAULT_CREATED_BY;
		}
		if (this.createdTimestamp == null) {
			this.createdTimestamp = new Date(System.currentTimeMillis());
		}
	}
}
