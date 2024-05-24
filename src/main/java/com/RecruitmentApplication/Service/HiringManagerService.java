package com.RecruitmentApplication.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.RecruitmentApplication.Entity.JobStatusTbl;
import com.RecruitmentApplication.Entity.NewJobRequestTbl;

public interface HiringManagerService {

	ResponseEntity<?> createNewJobRequest(NewJobRequestTbl newJobRequest);

	ResponseEntity<?> updateJobRequest(NewJobRequestTbl newJobRequest);

	List<NewJobRequestTbl> getAllJobRequests();

	List<NewJobRequestTbl> searchAndFilter(String jobId, String jobTitle, String department, String status,
			int numberOfPositions,String experience);

	NewJobRequestTbl getById(String jobId);
	
	 List<JobStatusTbl> getJobStatus();
}
