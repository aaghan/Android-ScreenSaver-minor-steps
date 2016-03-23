package missingdrop.stb_screensaver.Utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class DownloadUtil {

	public static String ServerUnrechable = "0";
	private static final String TAG = "com.newitventure.smartvision.movies.util.DownloadUtil";

	private String link;
	private String encoding = "utf-8";

	public String downloadStringContent(String link) {


				String responseString = "";

				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(link);

					HttpResponse httpResponse = httpClient.execute(httpGet);
					HttpEntity httpEntity = httpResponse.getEntity();

					responseString = EntityUtils.toString(httpEntity);
					return responseString;
					// Log.i(TAG, json + "------" );
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "UnsupportedEncodingException: " + e.toString());
					return ServerUnrechable;
				} catch (ClientProtocolException e) {
					Log.e(TAG, "ClientProtocolException: " + e.toString());
					return ServerUnrechable;
				} catch (IOException e) {
					Log.e(TAG, "IOException: " + e.toString());
					return ServerUnrechable;
				} catch (Exception e) {
					Log.e(TAG, "Exception: " + e.toString());
					return ServerUnrechable;
				}


	}



}
