package com.example.productmngmt.config.changelog;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmngmt.entity.Users;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;

@ChangeLog
public class DatabaseChangelog {


	@Transactional
	@ChangeSet(order = "005", id = "uatDefaultUser", author = "system")
	public void cleanMigrationData(MongoTemplate mongoTemplate) {
		System.out.println("asas");
		Users userInfo= new Users();
//		userInfo.setRole(RoleType.ADMINS.getName());
//		userInfo.setRoleId(2);
//		userInfo.setAccountId("t1037875");
//		userInfo.setEnabled(true);
//		userInfo.setUserName("Autoadmin");
		userInfo.setEmail("YWaCjQzp6gx4NZFJHhC/fJ++ccNxXACIHljwm/SM20k=");
//		userInfo.setDeleteFlag(false);
//		userInfo.setAttempts(0);
//		userInfo.setBlock(0);
//		userInfo.setSuperUser(true);
//		userInfo.setEulaFlag(true);
//		userInfo.setCreatedBy("system");
//		userInfo.setLastUpdatedBy("system");
//		userInfo.setCreatedOn(new Date());
//		userInfo.setLastUpdatedOn(new Date());
		mongoTemplate.insert(userInfo);
}
}
