@Test
public void searchByOneKeyword() {
   final String[] keywords = {"one", "two", "three"}
   final Collection<Asset> results = createListOfRandomAssets();
   Assets assets = new Assets(new FakeAPIClient(keywords, results));
   assertEquals(results, assets.search(keywords));
}