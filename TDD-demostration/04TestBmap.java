public class TestBmap{
	@Test
	public void mask(){
		Bmap bmap = new Bmap();
		bmap.addParameter(IPSEC_CERT_NAME);
		bmap.addParameter(IPSEC_ACTION_START_DAYS,0);
		bmap.addParameter(IPSEC_ACTION_START_HOURS,23);
		assertTrue(bmap.validate());
	}
}