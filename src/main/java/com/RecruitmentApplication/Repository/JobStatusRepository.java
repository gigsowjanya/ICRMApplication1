package com.RecruitmentApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RecruitmentApplication.Entity.JobStatusTbl;

public interface JobStatusRepository extends JpaRepository<JobStatusTbl, Long>{

}
