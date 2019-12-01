public interface UserRepository{
	public void save(User user);
	public User findById(Long id);
	public User findByUserName(String userName);
}