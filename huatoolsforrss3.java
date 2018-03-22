/*Once the SyndFeed instance is created, it is used to extract the metadata of the podcast(like title, description, author, copyright etc.): */


  @SuppressWarnings("unchecked")
public void setPodcastFeedAttributes(Podcast podcast,  boolean feedPropertyHasBeenSet) throws Exception {

	SyndFeed syndFeed = podcast.getPodcastFeed()

	if(syndFeed!=null){
		//set DESCRIPTION for podcast - used in search
		if(syndFeed.getDescription()!=null
				&& !syndFeed.getDescription().equals("")){
			String description = syndFeed.getDescription();
			//out of description remove tags if any exist and store also short description
			String descWithoutTabs = description.replaceAll("\\<[^>]*>", "");
			if(descWithoutTabs.length() > MAX_LENGTH_DESCRIPTION) {
				podcast.setDescription(descWithoutTabs.substring(0, MAX_LENGTH_DESCRIPTION));
			} else {
				podcast.setDescription(descWithoutTabs);
			}
		} else {
			podcast.setDescription("NO DESCRIPTION AVAILABLE for FEED");
		}

		//set TITLE - used in search
		String podcastTitle = syndFeed.getTitle();
		podcast.setTitle(podcastTitle);

		//set author
		podcast.setAuthor(syndFeed.getAuthor());

		//set COPYRIGHT
		podcast.setCopyright(syndFeed.getCopyright());

		//set LINK
		podcast.setLink(syndFeed.getLink());

		//set url link of the podcast's image when selecting the podcast in the main application - mostly used through 		SyndImage podcastImage = syndFeed.getImage();
		if(null!= podcastImage){
			if(podcastImage.getUrl() != null){
				podcast.setUrlOfImageToDisplay(podcastImage.getUrl());
			} else if (podcastImage.getLink() != null){
				podcast.setUrlOfImageToDisplay(podcastImage.getLink());
			} else {
				podcast.setUrlOfImageToDisplay(configBean.get("NO_IMAGE_LOCAL_URL"));
			}
		} else {
			podcast.setUrlOfImageToDisplay(configBean.get("NO_IMAGE_LOCAL_URL"));
		}

		podcast.setPublicationDate(null);//default value is null, if cannot be set

		//set url media link of the last episode - this is used when generating the ATOM and RSS feeds from the Start page for example
		for(SyndEntryImpl entry: (List)syndFeed.getEntries()){
			//get the list of enclosures
			List enclosures = (List) entry.getEnclosures();

			if(null != enclosures){
				//if in the enclosure list is a media type (either audio or video), this will set as the link of the episode
				for(SyndEnclosureImpl enclosure : enclosures){
					if(null!= enclosure){
						podcast.setLastEpisodeMediaUrl(enclosure.getUrl());
						break;
					}
				}
			}

			if(entry.getPublishedDate() == null){
				LOG.warn("PodURL[" + podcast.getUrl() + "] - " + "COULD NOT SET publication date for podcast, default date 08.01.1983 will be used " );
			} else {
				podcast.setPublicationDate(entry.getPublishedDate());
			}
			//first episode in the list is last episode - normally (are there any exceptions?? TODO -investigate)
			break;
		}
	}
}
