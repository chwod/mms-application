public class DocumentRepository {
   private static final String API_KEY = "d869db7fe62fb07c";
   private static String sessionToken;

   static {
      String serverHostName = System.getenv("ACL_SERVER_HOST");
      SessionClient api = new SessionClientImpl(serverHostName);
      sessionToken = api.openSession(API_KEY);
   }

   public DocumentRepository() {
      ...
   }
}