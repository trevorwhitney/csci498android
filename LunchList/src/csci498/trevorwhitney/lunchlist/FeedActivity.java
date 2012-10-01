package csci498.trevorwhitney.lunchlist;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;


public class FeedActivity extends ListActivity {
	
	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder
			.setTitle("Exception!")
			.setMessage(t.toString())
			.setPositiveButton("OK", null)
			.show();
	}
	
	private static class FeedTask extends 
			AsyncTask<String, Void, Void> {
		private Exception e = null;
		private FeedActivity activity = null;
		
		FeedTask(FeedActivity activity) {
			attach(activity);
		}
		
		@Override
		public Void doInBackground(String... urls) {
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet getMethod = new HttpGet(urls[0]);
				ResponseHandler<String> responseHandler = 
						new BasicResponseHandler();
				String responseBody = client.execute(getMethod, responseHandler);
				
				Log.d("FeedActivity", responseBody);
			}
			catch (Exception e) {
				this.e = e;
			}
			
			return null;
		}
		
		@Override
		public void onPostExectue(Void unused) {
			if (e == null) {
				// TODO
			}
			else {
				Log.e("LunchList", "Exception parsing feed", e);
				activity.goBlooey(e);
			}
		}
	}
}