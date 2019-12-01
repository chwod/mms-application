public class LoggerStub implements Logger {
	public void log(LogLevel level, String message) {
	}

	public LogLevel getLogLevel() {
		return LogLevel.WARN; // hard code return value.
	}
}