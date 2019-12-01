@Test
public void searchingByKeywords() {
   final String[] keywords = {"one", "two", "three"}
   final Collection<Asset> results = createListOfRandomAssets();
   APIClient.setInstance(new FakeAPIClient(keywords, results));
   Assets assets = new Assets();
   assertEquals(results, assets.search(keywords));
}