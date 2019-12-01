public class UUID {
   private String value;

   public UUID() {
      // First, obtain the computer's MAC address by
      // running ipconfig.exe and parsing its output
      long macAddress = 0;
      Process p = Runtime.getRuntime().exec(
               new String[] { "ipconfig", "/all" }, null);
      BufferedReader in = new BufferedReader(
               new InputStreamReader(p.getInputStream()));
      String line = null;
      while (macAddress == null &&
               (line = in.readLine()) != null) {
         macAddress = extractMACAddressFrom(line);
      }

      // Obtain the UTC time and rearrange
      // its bytes for a version 1 UUID
      long timeMillis = (System.currentTimeMillis() * 10000)
               + 0x01B21DD213814000L;
      long time = timeMillis << 32;
      time |= (timeMillis & 0xFFFF00000000L) >> 16;
      time |= 0x1000 | ((timeMillis >> 48) & 0x0FFF);

      ...
   }
}