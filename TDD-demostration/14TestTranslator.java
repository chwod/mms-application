public class TestTranslator{
	protected Mockery context;
	
	@Before
	public void createMockery() throws Exception{
		this.context = new JUnit4Mockey();
	}
	
	@Test
	public void useInternetForTranslation() throws Exception {
		final Internet internet = this.context.mock(Internet.class);
		
		this.context.checking(new Expectations() {{
			one(internet).get(with(containsString("l=dog")));
			will(returnValue("{\"translatedText\":\":\"dogg\"}"));
		}});
		Translator t = new Translator(internet);
		
		String translation = t.translate("dog",LANGUAGE.EN,LANGUAGE.SS);
		
		assertTrue("dogg",translation);
	}
}