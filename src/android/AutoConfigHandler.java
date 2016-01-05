package org.weeels.plugins.cardflight;

import org.apache.cordova.CallbackContext;
import com.getcardflight.interfaces.CardFlightAutoConfigHandler;
import android.util.Log;

public class AutoConfigHandler implements CardFlightAutoConfigHandler {

  private CDVCardFlight parent;

  public AutoConfigHandler(CDVCardFlight p) {
    parent = p;
  }

  @Override
  public void autoConfigProgressUpdate(int progress) {
    log("AutoConfig percent complete: "+progress+"%");
  }

  @Override
  public void autoConfigFinished() {
    log("AutoConfig successful");
    parent.autoConfigFinished("Initialization success");
  }

  @Override
  public void autoConfigFailed() {
    logError("AutoConfig failed");
    parent.autoConfigFailed("Initialization failed: AutoConfig failed");
  }

  private void log(String s) {
    Log.i("CDVCardFlightAutoConfigHandler", s);
  }

  private void logError(String s) {
    Log.e("CDVCardFlightAutoConfigHandler", s);
  }
}