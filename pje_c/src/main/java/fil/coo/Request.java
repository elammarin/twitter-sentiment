package fil.coo;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


public class Request {
		
	public void run(String rqst) throws Exception {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setHttpProxyHost("cache-etu.univ-lille1.fr");
		cb.setHttpProxyPort(3128);
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("Mf4LNKGlV3ZxJSr7m7yeZ5BmP")
		  .setOAuthConsumerSecret("IBkXvVM3E3YYBO833TyBYQDQnA5hcoJzDLrpQLFMUfxwGxBEJx")
		  .setOAuthAccessToken("1305467846349250560-IVnsd68g92PVE8VxwrEX3RoKMCx6bI")
		  .setOAuthAccessTokenSecret("UQdAXLAj4E2iauhCcvuvUd1agwKNOYzZBvQwScDjRvtI7");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		Query query = new Query(rqst);
	    QueryResult result;
	    result = twitter.search(query);
	    for (Status status : result.getTweets()) {
	        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText()); 
    	}
			
	}
	
	public static void main (String[] args) throws Exception {
		Request r= new Request();
		r.run("fance");
	}
}
