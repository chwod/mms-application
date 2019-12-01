public class UUID {
   private String value;

   public UUID() {
      long macAddress = acquireMacAddress();
      long timestamp = acquireUuidTimestamp();
      value = composeUuidStringFrom(macAddress, timestamp);
   }

   protected long acquireMacAddress() { ... }
   protected long acquireUuidTimestamp() { ... }

   private static String composeUuidStringFrom(
            long macAddress, long timestamp) { ... }
}