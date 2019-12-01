public String createTagName(String topic) {
   Timestamper c = new Timestamper();
   return topic + c.timestamp();
}