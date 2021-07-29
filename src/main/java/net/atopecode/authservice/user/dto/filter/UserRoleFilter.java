package net.atopecode.authservice.user.dto.filter;

import java.util.Arrays;

public class UserRoleFilter {

	public enum LogicComparation { AND, OR, DEFAULT }
	
	private String[] names;
	
	private LogicComparation logicComparation;
	
	public UserRoleFilter(String[] names, LogicComparation logicComparation) {
		this.names = (names != null) ? names : new String[0];
		this.logicComparation = (logicComparation != null) ? logicComparation : LogicComparation.DEFAULT;
	}

	public String[] getNames() {
		return names;
	}

	public LogicComparation getLogicComparation() {
		return logicComparation;
	}

	@Override
	public String toString() {
		return "UserRoleFilter [names=" + Arrays.toString(names) + ", logicComparation=" + logicComparation + "]";
	}
		
}
