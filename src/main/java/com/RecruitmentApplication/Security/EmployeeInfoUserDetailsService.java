package com.RecruitmentApplication.Security;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.RecruitmentApplication.Entity.EmployeeDetailsTbl;
import com.RecruitmentApplication.Repository.EmployeeRepository;

@Component
public class EmployeeInfoUserDetailsService implements UserDetailsService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EmployeeDetailsTbl empDetails = employeeRepository.findByEmployeeId(username);
		//Optional<EmployeeDetailsTbl> userInfo=Optional.of(empDetails);
        //return userInfo.map(EmployeeInfoUserDetails::new)
               // .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
		
		return new User(empDetails.getEmployeeId().equals(username)?username:(String)empDetails.getEmailId(), empDetails.getPassword(), new ArrayList<>());
	}

}
