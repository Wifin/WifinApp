package org.mixare;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.CheckBox;

/**
 * This is the main activity of mixare, that will be opened if mixare is
 * launched through the android.intent.action.MAIN the main tasks of this
 * activity is showing a prompt dialog where the user can decide to launch the
 * plugins, or not to launch the plugins. This class is also able to remember
 * those decisions, so that it can forward directly to the next activity.
 * 
 */
public class MainActivity extends Activity {

	private Context ctx;
	private final String usePluginsPrefs = "mixareUsePluginsPrefs";
	private final String usePluginsKey = "usePlugins";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		startActivity(new Intent(ctx, MixView.class));
		finish();
	}	

	/**
	 * Shows a dialog
	 */
	public void showDialog() {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle(R.string.launch_plugins);
		dialog.setMessage(R.string.plugin_message);

		final CheckBox checkBox = new CheckBox(ctx);
		checkBox.setText(R.string.remember_this_decision);
		dialog.setView(checkBox);		
		
		dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int whichButton) {
				processCheckbox(true, checkBox);
				finish();
			}
		});

		dialog.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int whichButton) {
				processCheckbox(true, checkBox);
				startActivity(new Intent(ctx, MixView.class));
				finish();
			}
		});

		dialog.show();
	}
	
	
	
	private void processCheckbox(boolean loadplugin, CheckBox checkBox){
		if(checkBox.isChecked()){
			SharedPreferences sharedPreferences = getSharedPreferences(usePluginsPrefs, MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(usePluginsKey, loadplugin);
			editor.commit();
		}
	}
		
}