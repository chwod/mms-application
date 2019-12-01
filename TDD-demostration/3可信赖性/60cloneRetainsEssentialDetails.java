@Test
public void cloneRetainsEssentialDetails() throws Exception {
   Document model = ModelFactory.createDocument();
   model.schedule(new Transaction("Test", date("2010-01-20"), 1));
   model= model.clone();
   Transaction tx = model.getScheduledTransactions().get(0);
}