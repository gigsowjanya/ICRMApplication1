package com.RecruitmentApplication.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.RecruitmentApplication.Entity.NewJobRequestTbl;
import com.RecruitmentApplication.Exception.RecruitmentException;
import com.RecruitmentApplication.Service.HiringManagerService;

@RestController
@RequestMapping("/api/v1.0/hiring")
@CrossOrigin(origins = "*")
public class HiringManagerController {

	@Autowired
	private HiringManagerService hmService;

	// added by Sowjanya for Creating new Job Request
	@PostMapping("/jobRequest")
	public ResponseEntity<?> createNewJobRequest(@RequestBody NewJobRequestTbl newJobRequest) {
		try {
			ResponseEntity<?> jobId = hmService.createNewJobRequest(newJobRequest);
			return jobId;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try after some times!");
		}
	}
	/*update job request by hiring manager*/
	@PutMapping("/updateJobRequest")
	public ResponseEntity<?> updateJobRequest(@RequestBody NewJobRequestTbl newJobRequest) {
		try {
			ResponseEntity<?> jobId = hmService.updateJobRequest(newJobRequest);
			return jobId;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try after some times!");
		}
	}
	//code added by Sowjanya for retreving all jobRequests
    @GetMapping("/all")
    public List<NewJobRequestTbl> getAllJobRequests() {
        return hmService.getAllJobRequests();
    }
    
 // code by sowjanya for search/filter operation
    @PostMapping("/job-requests/search-filter")
    public ResponseEntity<List<NewJobRequestTbl>> searchAndFilter(@RequestBody NewJobRequestTbl newJobRequest) {
        List<NewJobRequestTbl> result = hmService.searchAndFilter(
        		newJobRequest.getJobId(),
        		newJobRequest.getJobTitle(),
        		newJobRequest.getDepartment(),
        		newJobRequest.getStatus(),
        		newJobRequest.getNumberOfPositions(),
            newJobRequest.getExperience()
        );
        
        if (result != null && !result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    //code added by vinayak for getting one jobId 
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable String jobId) {
        NewJobRequestTbl job = hmService.getById(jobId);
        if (job != null) {
            return ResponseEntity.ok(job);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }
    }

    
    
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public String GetAuthorizedException(RecruitmentException errorMsg) {
	return errorMsg.getMessage();
}
}
