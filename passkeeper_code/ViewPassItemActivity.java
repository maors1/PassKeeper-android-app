package appdev.sapir.maor.passkeeper;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import appdev.sapir.maor.passkeeper.Database.PassItem;
import appdev.sapir.maor.passkeeper.Database.PassItemArrayAdapter;
import appdev.sapir.maor.passkeeper.Database.PasswordsDataSource;

/**
 *
 */
public class ViewPassItemActivity extends AppCompatActivity {

    private PasswordsDataSource dataSource;

    // UI references.
    private EditText mPassName;
    private EditText mPassUserName;
    private EditText mPassValue;
    private EditText mPassURL;

    private Intent loadIntent;
    private int position;
    private PassItemArrayAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataSource = PassKeeperMainActivity.dataSource;
        loadIntent = getIntent();
        position = loadIntent.getExtras().getInt("position");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pass_item);
        // Set up the login form.
        mPassName = (EditText) findViewById(R.id.evPassName);
        mPassUserName = (EditText) findViewById(R.id.evPassUserName);
        mPassValue = (EditText) findViewById(R.id.evPassValue);
        mPassURL = (EditText) findViewById(R.id.evPassURL);
        loadPassItemValue(position);
        adapter = (PassItemArrayAdapter) PassKeeperMainActivity.mListView.getAdapter();
    }

    public void onClick(View view) {
        dataSource.open();
        switch (view.getId()) {
            case R.id.btnUpdate:
                if (!(mPassName.getText().toString().isEmpty() || mPassValue.getText().toString().isEmpty())) {
                    PassItem itemToUpdate = (PassItem) PassKeeperMainActivity.mListView.
                            getAdapter().getItem(position);
                    itemToUpdate.setPassName(mPassName.getText().toString());
                    itemToUpdate.setUserName(mPassUserName.getText().toString());
                    itemToUpdate.setPassValue(mPassValue.getText().toString());
                    itemToUpdate.setPassUrl(mPassURL.getText().toString());
                    if (true == dataSource.updatePassItem(itemToUpdate)) {
                        adapter.notifyDataSetChanged();
                        this.onBackPressed();
                    }
                    break;
                }
            case R.id.btnCancel:
                this.onBackPressed();
                break;
            case R.id.btnDelete:
                setDialog();
                break;
        }
    }

    private void setDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Password");
        builder.setMessage("Do you want delete the password?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePassItem();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loadPassItemValue(int position) {
        PassItem passItemToLoad = (PassItem) PassKeeperMainActivity.mListView.getAdapter().getItem(position);
        mPassName.setText(passItemToLoad.getPassName());
        mPassUserName.setText(passItemToLoad.getUserName());
        mPassValue.setText(passItemToLoad.getPassValue());
        mPassURL.setText(passItemToLoad.getPassUrl());
    }

    private void deletePassItem() {
        if (PassKeeperMainActivity.mListView.getAdapter().getCount() > 0) {
            dataSource.deletePassItem(adapter.getItem(position));
            adapter.originalData.remove(adapter.getItem(position));
            onBackPressed();
        }
    }

}

