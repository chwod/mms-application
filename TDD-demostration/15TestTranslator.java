public class TestTranslator{
	protected Mockery context;
	
	@Before
	public void createMockery() throws Exception{
		this.context = new JUnit4Mockey();
	}
	
	@Test
	public void useInternetForTranslation() throws Exception {
		final Internet internet = this.context.mock(Internet.class);
		
		when(internet.get(argThat(containsString("l=dog"))))
		.thenReturn("{\"translatedText\":\":\"dogg\"}");

		Translator t = new Translator(internet);
		
		String translation = t.translate("dog",LANGUAGE.EN,LANGUAGE.SS);
		
		assertTrue("dogg",translation);
	}
}