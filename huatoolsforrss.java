/* Using the approach just mentioned works fine for most of the podcast feeds out there, but for some, mean exceptions like "Content is not allowed in prolog" or "Invalid byte 2 of 3-byte UTF-8 sequence" started to occur. To tackle these exceptions I replaced the XmlReader with InputSource, which solved most of the problems – thank you Paŭlo Ebermann on StackOverflow  researching into this. The following code snippet presents how this is used to parse the feeds: */


public SyndFeed getSyndFeedForUrl(String url) throws MalformedURLException, IOException, IllegalArgumentException, FeedException {

	SyndFeed feed = null;
	InputStream is = null;

	try {

		URLConnection openConnection = new URL(url).openConnection();
		is = new URL(url).openConnection().getInputStream();
		if("gzip".equals(openConnection.getContentEncoding())){
			is = new GZIPInputStream(is);
		}
		InputSource source = new InputSource(is);
		SyndFeedInput input = new SyndFeedInput();
		feed = input.build(source);

	} catch (Exception e){
		LOG.error("Exception occured when building the feed object out of the url", e);
	} finally {
		if( is != null)	is.close();
	}

	return feed;
}
