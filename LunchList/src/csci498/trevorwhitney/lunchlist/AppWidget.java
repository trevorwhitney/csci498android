package csci498.trevorwhitney.lunchlist;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class AppWidget extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context ctxt, AppWidgetManager mgr, 
			int[] appwidgetIds) {
		ctxt.startService(new Intent(ctxt, WidgetService.class));
	}
}