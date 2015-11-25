package com.magine.rxhackday;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.magine.rxhackday.api.RestService;
import com.magine.rxhackday.api.Result;
import com.magine.rxhackday.api.SearchHit;

import java.util.concurrent.TimeUnit;

import rx.Observable;
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

		Subscriber subscriber = new Subscriber<SearchHit>() {
			@Override
			public final void onCompleted() {
				Toast.makeText(MainActivity.this, "Search complete", Toast.LENGTH_SHORT).show();
			}

			@Override
			public final void onError(Throwable e) {
				Log.e("RxHackDay", e.getMessage());
			}

			@Override
			public final void onNext(SearchHit response) {
				Log.i(TAG, "Next: " + response.title);
				resultText.setText(resultText.getText() + "\n" + response.title);
			}
		};

		RxTextView.textChanges(mEditText)
				  .filter(e -> e.length() > 2)
				  .debounce(1000, TimeUnit.MILLISECONDS)
				  .flatMap(e -> RestService.getCoolService().search(e.toString()))
				  .observeOn(AndroidSchedulers.mainThread())
				  .flatMap(response -> Observable.from(response.responseData.results))
				  .flatMap(this::makeHit)
				  .subscribe(subscriber);
	}

	private void printText(String text) {
		Log.i(TAG, text);
	}

	private Observable<SearchHit> makeHit(Result result) {
		return Observable.create(new Observable.OnSubscribe<SearchHit>() {
			@Override
			public void call(Subscriber<? super SearchHit> subscriber) {
				subscriber.onNext(new SearchHit(result.title));
				subscriber.onCompleted();
			}
		});
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
		RestService.getCoolService().search("Christmas")
				   .subscribeOn(Schedulers.newThread())
				   .observeOn(AndroidSchedulers.mainThread())
				   .subscribe(response -> Log.i(TAG, response.toString()));
	}
}
