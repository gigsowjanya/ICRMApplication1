package com.RecruitmentApplication.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.RecruitmentApplication.Entity.NewJobRequestTbl;

@Repository
public interface HiringManagerRepository extends JpaRepository<NewJobRequestTbl,Long> {

	NewJobRequestTbl findByJobId(String jobId);

	// code by sowjanya for search/filter operation
		 @Query("SELECT j FROM NewJobRequestTbl j WHERE " +
		            "(:jobId IS NULL OR j.jobId = :jobId) AND " +
		            "(:jobTitle IS NULL OR j.jobTitle LIKE %:jobTitle%) AND " +
		            "(:department IS NULL OR j.department = :department) AND " +
		            "(:numberOfPositions = 0 OR j.numberOfPositions = : numberOfPositions) AND" +
		            "(:experience IS NULL OR j.experience = experience) AND " +
		            "(:status IS NULL OR j.status = :status)")
		 
		    List<NewJobRequestTbl> searchAndFilter(
		            String jobId, 
		            String jobTitle, 
		            String department, 
		            String status,
		            int numberOfPositions,
		            String experience
		            
		            
		    );



}
