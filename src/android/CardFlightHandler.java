package org.weeels.plugins.cardflight;

import com.getcardflight.models.Card;
import com.getcardflight.models.CardFlightError;
import com.getcardflight.interfaces.*;
import android.util.Log;

public class CardFlightHandler implements CardFlightDeviceHandler {
  
  private Card card;
  private CDVCardFlight parent;

  private boolean isConnected;

  public CardFlightHandler(CDVCardFlight p) {
    parent = p;
  }

  public void resetCard() {
    card = null;
  }
  
  public Card getCard(){
    return card;
  }

  @Override
  public void readerBatteryLow() {
    log("readerBatteryLow callback");
    parent.readerBatteryLow();
  }

  @Override
  public void readerCardResponse(Card c, CardFlightError error) {
    if (error == null) {
      log("readerCardResponse callback");
      card = c;
      parent.cardReadCallback(true, "Success");
    } else {
      log("readerCardResponse callback with error: "+error.getMessage());
      parent.cardReadCallback(false, "Error reading card: "+error.getMessage());
    }
  }

  @Override
  public void readerIsConnected(boolean connected, CardFlightError error) {
    if (error == null) {
      parent.readerConnectedCallback();
    } else {
      parent.readerFail(error.getMessage());
    }
  }

  @Override
  public void readerIsConnecting() {
    log("readerIsConnecting callback");
    parent.readerConnectingCallback();
  }

  @Override
  public void readerIsAttached() {
    log("readerIsAttached callback");
    isConnected = true;
    parent.readerAttachedCallback();
  }

  @Override
  public void readerIsDisconnected() {
    log("readerIsDisconnected callback");
    // if (isConnected) parent.initializeReader();
    isConnected = false;
    parent.readerDisconnectedCallback();
  }

  @Override
  public void readerSwipeDetected() {
    log("readerSwipeDetected callback");
    parent.onSwipeDetected();
  }

  @Override
  public void readerSwipeDidCancel() {
    log("readerSwipeDidCancel callback, no cordova-layer method to call");
  }

  @Override
  public void readerNotDetected() {
    log("readerNotDetected callback, no cordova-layer method to call");
    isConnected = false;
  }

  @Override
  public void deviceBeginSwipe() {
    // Deprecated in SDK v3.0.4
  }


  @Override
  public void readerFail(String errorMessage, int errorCode) {
    // Deprecated in SDK v3.0.4
    logError("readerFail callback code:"+errorCode+", with message: " + errorMessage);
    // if (isConnected) parent.initializeReader();
    isConnected = false;
  }

  private void log(String s) {
    Log.i("CDVCardFlightDeviceHandler", s);
  }

  private void logError(String s) {
    Log.e("CDVCardFlightDeviceHandler", s);
  }
}