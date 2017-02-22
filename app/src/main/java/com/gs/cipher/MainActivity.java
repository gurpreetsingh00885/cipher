package com.gs.cipher;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


public class MainActivity extends Activity
{

	private EditText edittext1;
    private boolean paused=false;
	private LinearLayout linear1;
	private EditText edittext2;
	private Button button2;
	private Button button3;
	private String text = "";
    private android.content.ClipboardManager clipboard;
	private Intent svc;
	private ScrollView scroll1;
	private Dialog dialog;
	private LayoutInflater factory;
	private View titleView;
	private boolean exitNow = false;
    private boolean showingInfo=false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		initialize();
		initializeLogic();
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	    stopService(svc);
	}

	@Override
	public void onUserInteraction()
	{
		// TODO: Implement this method
		stopService(svc);
		super.onUserInteraction();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		startService(svc);

	}

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus && !showingInfo && !exitNow && !paused){startService(svc);}
    }

    /*protected void onDestroy(Bundle savedInstanceState)
         {
         // TODO: Implement this method
         showMessage("Resumed");
         super.onResume();
         }*/
	private void  initialize()
	{
		factory =  getLayoutInflater();
		titleView =  factory.inflate(R.layout.title, null);
		edittext1 = (EditText) findViewById(R.id.edittext1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		edittext2 = (EditText) findViewById(R.id.edittext2);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
        svc = new Intent(this, OverlayShowingService.class);
        dialog = new Dialog(this);
		scroll1 = (ScrollView) dialog.findViewById(R.id.scroll1);




		button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _v)
				{
					text = edittext1.getText().toString();
					text = convert(text);
					edittext2.setText(text);
					ClipData clip = ClipData.newPlainText("output", edittext2.getText().toString());
					clipboard.setPrimaryClip(clip);
				}
			});


		/*close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _v)
				{
					showMessage("Thanks for using Cipher.");
					stopService(svc);
				    finish();
				}
			});*/


		button3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _v)
				{ 
					text = edittext1.getText().toString();
					if (chk(text))
					{
						text = deconvert(text.substring(0, text.length() - 3), Integer.parseInt(text.substring(text.length() - 3, text.length())));
						edittext2.setText(text);
						ClipData clip = ClipData.newPlainText("output", edittext2.getText().toString());
						clipboard.setPrimaryClip(clip);
					}
					else
					{
						showMessage("Sorry. The Ciphertext is invalid.");
					}

				}
			});



		/*info.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _v)
				{

					dialog.setContentView(R.layout.custom);
					dialog.setTitle("Help");
					dialog.show();



					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v)
							{
								dialog.dismiss();
							}
						});
				}
			});
		*/
	}


	private void  initializeLogic()
	{
	}

	public void pasteInput(View v){
		ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
		if (item.getText() != null)
		{
			edittext1.setText(item.getText());
		}
		else showMessage("Clipboard is empty.");
	}

	public void copyOutput(View v){

	}

	public void close(View v)
	{
		stopService(svc);
		exitNow = true;
		finish();
	}

	public void clearAll(View v){
		edittext1.setText("");
		edittext2.setText("");
	}

	public void info(View v)
	{
        showingInfo =  true;
		dialog.setContentView(R.layout.custom);
		dialog.setTitle("Help");
		dialog.show();



		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
                showingInfo = false;
			}
		});
	}

	private void showMessage(String _s)
	{
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}

    private String deconvert(String inputText, int times)
	{
		String Key = "ABCDEFGHIJKL MNOPQRSTUVWXYZabcdefghijklmnop\\qrstuvwxy\"z1234567890:;-=+_)(*&^%$#@!`~<>,./|?][\'{\n}\t";
		String key = "qpwoeirutyalskdjfhgzmxncb\"v GFHDJSKA\\LTYRUEIWOQPVCBXNZM8567904321\':;|{[}]!@#$%(*&^)_-=+`/~<>,.\n?\u2122";
		String finalText="";
		String c="";
		for (int i = 0; i < inputText.length(); i++)
		{
		    try
			{

				c = Key.substring(key.indexOf(inputText.substring(i, i + 1)), key.indexOf(inputText.substring(i, i + 1)) + 1);
				for (int j = 0 ; j < times; j++)
					c = Key.substring(key.indexOf(c), key.indexOf(c) + 1);
				finalText += c;
		    }
			catch (RuntimeException e1)
			{
				finalText += c;
			}
		}
		return finalText;
	}

	private boolean chk(String inp)
	{
		if (inp.length() < 4)
			return false;
		if (Character.isDigit(inp.charAt(inp.length() - 1)) && Character.isDigit(inp.charAt(inp.length() - 2)) && Character.isDigit(inp.charAt(inp.length() - 3)))
			return true;
		return false;
	}
	private String convert(String inputText)
	{
		String key = "ABCDEFGHIJKL MNOPQRSTUVWXYZabcdefghijklmnop\\qrstuvwxy\"z1234567890:;-=+_)(*&^%$#@!`~<>,./|?][\'{\n}\t";
		String Key = "qpwoeirutyalskdjfhgzmxncb\"v GFHDJSKA\\LTYRUEIWOQPVCBXNZM8567904321\':;|{[}]!@#$%(*&^)_-=+`/~<>,.\n?\u2122";
		String finalText="";
		String c="";
		int times = random_dig();
		for (int i = 0; i < inputText.length(); i++)
		{
			try
			{
				c = Key.substring(key.indexOf(inputText.substring(i, i + 1)), key.indexOf(inputText.substring(i, i + 1)) + 1);
				for (int j = 0; j < times; j++)
					c = Key.substring(key.indexOf(c), key.indexOf(c) + 1);
				finalText += c;
			}
		    catch (RuntimeException e1)
			{
				showMessage(String.format("Couldn't recognize '%c'", inputText.charAt(i)));
			    finalText += c;
			}
		}
		finalText += times;
		return finalText;	
	}
	private int random_dig()
	{
		int d = (int) Math.round(Math.random() * 1000);
		while (true)
		{
			if (d < 100 || d > 999) d = (int) Math.round(Math.random() * 1000);
			else return d;
		}
	}
}



