package science.bbbar326.twitter;

import java.text.SimpleDateFormat;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class App {
	public static void main(String[] args) throws TwitterException
	{
		Twitter twitter = new TwitterFactory().getInstance();

		// 検索
		Query query = new Query();
		query.setQuery("from:realDonaldTrump");
		query.setCount(10);

		QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		List<Status> list = result.getTweets();

		for (Status s : list) {
			System.out.println(new SimpleDateFormat("<<< yyyy年MM月dd日 hh時mm分ss秒 >>>").format(s.getCreatedAt()));
			System.out.println(s.getText());
		}

	}

}
