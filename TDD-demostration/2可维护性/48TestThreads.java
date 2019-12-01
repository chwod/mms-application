public class TestThreads1 {
	private final int threads = 10;
	private final int callsPerThread = 100;
	private Counter counter;

	@Before
	public void setUp() throws Exception {
		counter = new Counter();
	}

	@Test
	public void concurrentAccessFromMultipleThreads() throws Exception {
		final CountDownLatch allThreadsComplete = new CountDownLatch(threads);
		final Set<Long> values = new HashSet<Long>();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < callsPerThread; i++) {
					values.add(counter.getAndIncrement());
				}
				allThreadsComplete.countDown();
			}
		};
		for (int i = 0; i < threads; i++) {
			new Thread(runnable).start();
		}
		allThreadsComplete.await(10, TimeUnit.SECONDS);
		int expectedNoOfValues = threads * callsPerThread;
		assertEquals(expectedNoOfValues, values.size());
	}
}
