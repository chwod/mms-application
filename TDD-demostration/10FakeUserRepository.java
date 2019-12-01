public class FakeUserRepository implements UserRepository{
	private List<User> users = new ArrayList<User>();
	
	public void save(User user) {
		this.users.add(user);
	}
	
	public void findById(Long id) {
		for(User user : this.users) {
			if(user.getUserId == id) {
				return user
			}
		}
		return null;
	}
	
	public void findByUserName(Strng userName) {
		for(User user : this.users) {
			if(user.getUserName().equals(userName)) {
				return user;
			}
		}
		return null;
	}
}