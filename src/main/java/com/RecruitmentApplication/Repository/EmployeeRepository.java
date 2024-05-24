package com.RecruitmentApplication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.RecruitmentApplication.Entity.EmployeeDetailsTbl;

public interface EmployeeRepository extends JpaRepository<EmployeeDetailsTbl,Long> {

	EmployeeDetailsTbl findByEmailId(String emailId);
	@Query(value="select * from employee_details_tbl where (email_id=:username) or (employee_id = :username)",nativeQuery=true)
	EmployeeDetailsTbl findByEmployeeId(String username);
	Optional<EmployeeDetailsTbl> findByPhoneNumber(String username);

}
