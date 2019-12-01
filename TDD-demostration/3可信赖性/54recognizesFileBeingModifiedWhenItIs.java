// @Test
// public void recognizesFileBeingModifiedWhenItIs() throws
// Exception {
// File newFile = File.createTempFile(getName(), ".tmp");
// FileAppendingThread thread = new FileAppendingThread(newFile);
// thread.start();
// thread.waitUntilAppendingStarts();
// try {
// Thread.sleep(200);
// assertTrue(IO.fileIsBeingModified(newFile));
// } finally {
// thread.interrupt();
// }
// }