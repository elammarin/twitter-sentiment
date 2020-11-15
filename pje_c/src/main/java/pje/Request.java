package pje;

import java.util.Map;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


public class Request {
	
	public Twitter getTwitterInstance() {
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
		return twitter;
	}
	
	public int getRemainingRequest() throws TwitterException {
		int result = 0; 
		Twitter twitter = this.getTwitterInstance();
		 Map<String ,RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
		 for (String endpoint : rateLimitStatus.keySet()) {
		   RateLimitStatus status = rateLimitStatus.get(endpoint);
		   //System.out.println("Endpoint: " + endpoint);
		   //System.out.println(" Limit: " + status.getLimit());
		   //System.out.println(" Remaining: " + status.getRemaining());
		   //System.out.println(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
		   //System.out.println(" SecondsUntilReset: " + status.getSecondsUntilReset());
		   if (endpoint.contains("/search/tweets")) {
			   	result = (status.getRemaining());
		   }
		 }
		 return result;
	}
		
	public QueryResult run(String rqst) throws Exception {
		Twitter twitter = this.getTwitterInstance();
		Query query = new Query(rqst);
		query.setCount(100);
	    QueryResult result;
	    result = twitter.search(query);
	    //for (Status status : result.getTweets()) {
	    //    System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText()); 
    	//}
	    return result;
			
	}
	
	public static void main (String[] args) throws Exception {
		Request r= new Request();
		r.run("France");
		System.out.print(r.getRemainingRequest());
	}
}
