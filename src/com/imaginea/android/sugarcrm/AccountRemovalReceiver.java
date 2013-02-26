package com.imaginea.android.sugarcrm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AccountRemovalReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		AccountRemovalService.processBroadcastIntent(context, intent);

	}

}
