package com.example.productmngmt.config.changelog;

import java.util.Collections;

import org.springframework.transaction.annotation.Transactional;

import com.example.productmngmt.entity.Roles;
import com.example.productmngmt.entity.Users;
import com.example.productmngmt.repo.UserRepo;

import io.changock.migration.api.annotations.ChangeLog;
import io.changock.migration.api.annotations.ChangeSet;
@ChangeLog
public class DatabaseChangelog {

	@Transactional
	@ChangeSet(order = "001", id = "defaultUser", author = "system")
	public void addDefaultUser(UserRepo userRepo) {
		Users user = new Users();
		user.setUuid(1L);
		user.setFirstName("mQ2hPvNqrShPOmLRtiqvlw==");
		user.setLastName("rvlMgXTLgUmnuXZvPi8ZdA==");
		user.setEmail("VI6egjwQVaxYbUodL+MeOg==");
		user.setAddress("AbyxqyVpHjhYB4WwbCtcCA==");
		user.setPassword("$2a$10$a9hMAms0VN.kJ5lVvKQ6NOv8ylLaKvm66l/TyyXsV.hZjGDgpFjqO");
		user.setPhoneNumber("pe0P5BZvxnA3AlcklHlpjA==");
		user.setRoles(Collections.singletonList(new Roles("cqKl2KMqYlESk2M6wUoUdQ==")));

		userRepo.save(user);
	}
}
