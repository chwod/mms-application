public class DLog{
	private final DLogTarget[] targets;
	
	public DLog(DLogTarget... targets) {
		this.targets = targets;
	}
	
	public wirte(Level level, String message) {
		for(DLogTarget target : targets) {
			garget.write(level, message);
		}
	}
}

public interface DLogTarget{
	void write(Level level, String message);
}