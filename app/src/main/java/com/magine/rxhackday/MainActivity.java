package com.magine.rxhackday;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.magine.rxhackday.api.CoolService;
import com.magine.rxhackday.api.Response;
import com.magine.rxhackday.api.RestService;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private EditText mEditText;
	private TextView resultText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
			}
		});

		configTextField();
	}

	private void configTextField() {
		mEditText = (EditText) findViewById(R.id.search_edittext);
		resultText = (TextView) findViewById(R.id.result_text);

		Subscriber subscriber = new Subscriber<Response>() {
			@Override
			public final void onCompleted() {
				// do nothing
			}

			@Override
			public final void onError(Throwable e) {
				Log.e("RxHackDay", e.getMessage());
			}

			@Override
			public final void onNext(Response response) {
				Log.i(TAG, "Next: " + response.toString());
				resultText.setText(response.responseData.results.toString());
			}
		};

		RxTextView.textChanges(mEditText)
				.debounce(300, TimeUnit.MILLISECONDS)
				.doOnCompleted(() -> printText("Searching "))
				.flatMap(e -> RestService.getCoolService().search(e.toString()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	private void printText(String text) {
		Log.i(TAG, text);
	}

	private void performSearch(String query) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void fireButton(View view) {
		RestService.getCoolService().search("Christmas").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> Log.i(TAG, response.toString()));
	}
}
