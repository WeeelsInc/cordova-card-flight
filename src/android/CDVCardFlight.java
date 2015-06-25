package org.weeels.plugins.cardflight;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.getcardflight.models.CardFlight;
import com.getcardflight.models.Reader;

import android.util.Log;

public class CDVCardFlight extends CordovaPlugin {

  private Reader reader;
  private CardFlightHandler handler;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    handler = new CardFlightHandler(cordova);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    boolean success = true;

    if (action.equals("authorizeCardFlightAccount")) {
      this.authorizeCardFlightAccount(args.getString(0), args.getString(1), callbackContext);
    } else if (action.equals("initializeReader")) {
      this.initializeReader(callbackContext);
    } else if (action.equals("onReaderAttached")) {
      handler.onReaderConnected(callbackContext);
    } else if (action.equals("onReaderConnecting")) {
      handler.onReaderConnecting(callbackContext);
    } else if (action.equals("onSwipeDetected")) {
      handler.onSwipeDetected(callbackContext);
    } else if (action.equals("onBatteryLow")) {
      handler.onBatteryLow(callbackContext);
    } else if (action.equals("onReaderDisconnected")) {
      handler.onReaderDisconnected(callbackContext);
    } else if (action.equals("tokenizeLastSwipe")) {
      handler.tokenizeCard(callbackContext);
    } else if (action.equals("onReaderConnected")) {
      handler.onReaderConnected(callbackContext);
    } else if (action.equals("watchForSwipe")) {
      this.watchForSwipe(callbackContext);
    } else if (action.equals("onCardRead")) {
      handler.onCardRead(callbackContext);
    } else {
      success = false;
    }
    
    return success;
  }

  private void authorizeCardFlightAccount(String apiToken, String stripeMerchantToken, CallbackContext callbackContext) {
    if (apiToken == null || stripeMerchantToken == null) {
      callbackContext.error("Need to send both an api token and a stripe merchant token to authorize cardflight");
    }

    CardFlight.getInstance().setApiTokenAndAccountToken(apiToken, stripeMerchantToken);
  }

  private void initializeReader(CallbackContext callbackContext) {
    reader = new Reader(this.cordova.getActivity().getApplicationContext(), handler);
    callbackContext.success("CardFlight reader initialized");
  }

  private void watchForSwipe(CallbackContext callbackContext) {
    handler.resetCard();
    reader.beginSwipe();
    callbackContext.success("CardFlight reader awaiting swipe");
  }
}