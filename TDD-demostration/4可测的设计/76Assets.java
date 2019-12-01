public class Assets {
   public Collection<Asset> search(String... keywords) {
      APIRequest searchRequest = createSearchRequestFrom(keywords);
      APICredentials credentials = Configuration.getCredentials();
      APIClient api = APIClient.getInstance(credentials);
      return api.search(searchRequest);
   }

   private APIRequest createSearchRequestFrom(String... keywords) {
      // omitted for brevity
   }
}