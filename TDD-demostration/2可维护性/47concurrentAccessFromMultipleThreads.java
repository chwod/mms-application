
	@Test
	public  void concurrentAccessMultipleThreads() throws Exception {
		final Counter counter = new Counter();
		
		final int callsPerThread = 100;
		final Set<Long> values = new HashSet<>();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for(int i = 0;i < callsPerThread; i++) {
					values.add(counter.getAndIncrement());
				}
			}
		};
		
		int threads = 10;
		for(int i=0;i<threads;i++) {
			new Thread(runnable).start();
		}
		
		Thread.sleep(500);
		
		int expectedNoOfValues = threads * callsPerThread;
		assertEquals(expectedNoOfValues, values.size());
		
	}