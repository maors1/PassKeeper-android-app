package appdev.sapir.maor.passkeeper;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import appdev.sapir.maor.passkeeper.Database.PassItem;
import appdev.sapir.maor.passkeeper.Database.PassItemArrayAdapter;
import appdev.sapir.maor.passkeeper.Database.PasswordsDataSource;

/**
 *
 */
public class AddNewPassActivity extends AppCompatActivity {

    private PasswordsDataSource datasource = PassKeeperMainActivity.dataSource;

    private Intent intent = getIntent();

    private AutoCompleteTextView mPassName;

    private AutoCompleteTextView mPassUserName;

    private AutoCompleteTextView mPassValue;

    private AutoCompleteTextView mPassURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pass);

        mPassName = (AutoCompleteTextView) findViewById(R.id.autoCompletePassName);
        mPassUserName = (AutoCompleteTextView) findViewById(R.id.autoCompletePassUserName);
        mPassValue = (AutoCompleteTextView) findViewById(R.id.autoCompletePassValue);
        mPassURL = (AutoCompleteTextView) findViewById(R.id.autoCompletePassURL);

        ArrayList<String> values = datasource.getPasswords();
        mPassValue.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values));
        mPassValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mPassValue.showDropDown();

            }
        });

        mPassValue.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPassValue.showDropDown();
                return false;
            }
        });

    }

    public void addPassItem(View view) {
        datasource.open();
        PassItemArrayAdapter adapter = (PassItemArrayAdapter) PassKeeperMainActivity.mListView.getAdapter();
        if (!(mPassName.getText().toString().isEmpty() || mPassValue.getText().toString().isEmpty())) {
            PassItem passItem = datasource.createPassItem(mPassName.getText().toString()
                    , mPassUserName.getText().toString(), mPassValue.getText().toString(),
                    mPassURL.getText().toString());
            adapter.originalData.add(passItem);
            adapter.notifyDataSetChanged();
            this.onBackPressed();
        } else {
            Toast.makeText(this, R.string.err_msg_add_new_pass_item, Toast.LENGTH_SHORT).show();
        }
    }

}

