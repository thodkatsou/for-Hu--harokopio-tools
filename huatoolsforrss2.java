/*If for some reason ("Content is not allowed in prolog" or "Invalid byte 2 of 3-byte UTF-8 sequence" etc.),  you cannot parse the feed from the online like via its URL, you can store to a local file, process it and modify the encoding for your needs (very easy with Notepad++  for example ) and parse it from there : */









    public SyndFeed getSyndFeedFromLocalFile(String filePath)
		throws MalformedURLException, IOException,
		IllegalArgumentException, FeedException {

	SyndFeed feed = null;
	FileInputStream fis = null;
	try {
		fis = new FileInputStream(filePath);
		InputSource source = new InputSource(fis);
		SyndFeedInput input = new SyndFeedInput();
		feed = input.build(source);
	} finally {
		fis.close();
	}

	return feed;
}






     
