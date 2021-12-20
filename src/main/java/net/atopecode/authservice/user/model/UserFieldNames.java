package net.atopecode.authservice.user.model;

public class UserFieldNames {
	
	private UserFieldNames () {
		//Empty constructor.
	}
	
	public static final String ENTITY = "User";
	
	//User and Dto fields:
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email";
	public static final String REAL_NAME = "realName";
	public static final String REL_USER_ROLE = "relUserRole";
	
	public static final String NM_NAME = "nm_name";
	public static final String NM_EMAIL = "nm_email";
	public static final String NM_REAL_NAME = "nm_realName";
	
	
	//User Dto only fields:
	public static class Dto{
		private Dto() {
			//Empty Constructor.
		}
		
		public static final String ROLES = "roles";
	}
}