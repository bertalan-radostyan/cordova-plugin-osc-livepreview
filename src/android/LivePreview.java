package com.radostyan.cordova.livepreview;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Runnable;

import com.radostyan.cordova.livepreview.MJpegInputStream;

public class LivePreview extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("getLivePreview")) {
			String ip = args.getString(0);
			try {
				getLivePreview(callbackContext, ip);
			} catch (Exception e) {
				callbackContext.error(e.getMessage());
			}

			return true;
		}

		return false;
	}

	private void getLivePreview(CallbackContext callbackContext, String ip) throws IOException, JSONException, MalformedURLException {
		URL url = new URL(ip + "osc/command/execute");
		HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();
		postConnection.setDoInput(true);
		postConnection.setDoOutput(true);

		JSONObject input = new JSONObject();
		input.put("name", "camera.getLivePreview");

		OutputStream os = postConnection.getOutputStream();
		os.write(input.toString().getBytes());

		postConnection.connect();
		os.flush();
		os.close();

		InputStream is = postConnection.getInputStream();
		final MJpegInputStream mjis = new MJpegInputStream(is);

		cordova.getThreadPool().execute(new Runnable() {
			@Override
            public void run() {
                boolean keepRunning = true;
				while (keepRunning) {
					try {
						callbackContext.success(mjis.readMJpegFrame());
					} catch (IOException e) {
						keepRunning = false;
					}
				}
            }
        });
	}
}