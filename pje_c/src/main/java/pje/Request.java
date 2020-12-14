package pje;

import java.util.Map;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Arthur Assima & Nordine El Ammari
 */
public class Request {
	
	/**
	 * @return the twitter instance
	 */
	public Twitter getTwitterInstance() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		//cb.setHttpProxyHost("cache-etu.univ-lille1.fr");
		//cb.setHttpProxyPort(3128);
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("Mf4LNKGlV3ZxJSr7m7yeZ5BmP")
		  .setOAuthConsumerSecret("IBkXvVM3E3YYBO833TyBYQDQnA5hcoJzDLrpQLFMUfxwGxBEJx")
		  .setOAuthAccessToken("1305467846349250560-IVnsd68g92PVE8VxwrEX3RoKMCx6bI")
		  .setOAuthAccessTokenSecret("UQdAXLAj4E2iauhCcvuvUd1agwKNOYzZBvQwScDjRvtI7");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}
	
	/**
	 * @return the number of available requests left
	 * @throws TwitterException
	 */
	public int getRemainingRequest() throws TwitterException {
		int result = 0; 
		Twitter twitter = this.getTwitterInstance();
		 Map<String ,RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
		 for (String endpoint : rateLimitStatus.keySet()) {
		   RateLimitStatus status = rateLimitStatus.get(endpoint);
		   if (endpoint.contains("/search/tweets")) {
			   	result = (status.getRemaining());
		   }
		 }
		 return result;
	}
		
	/**
	 * @param rqst the request
	 * @return the twitter search
	 * @throws Exception
	 */
	public QueryResult run(String rqst) throws Exception {
		Twitter twitter = this.getTwitterInstance();
		Query query = new Query(rqst);
		query.setCount(20);
	    QueryResult result;
	    result = twitter.search(query);

	    return result;
			
	}
	
	public static void main (String[] args) throws Exception {
		Request r= new Request();
		r.run("France");
		System.out.print(r.getRemainingRequest());
	}
}
