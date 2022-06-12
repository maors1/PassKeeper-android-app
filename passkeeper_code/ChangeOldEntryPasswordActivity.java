package appdev.sapir.maor.passkeeper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 */
public class ChangeOldEntryPasswordActivity extends AppCompatActivity {

    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mPassHint;
    private EditText mOldPassword;
    private SharedPreferences data;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = getSharedPreferences("Pass_Keeper_data", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_old_entry_password);
        mPassword = (EditText) findViewById(R.id.etpEnterPassword);
        mConfirmPassword = (EditText) findViewById(R.id.etpConfirmPassword);
        mPassHint = (EditText) findViewById(R.id.et_pass_hint);
        mOldPassword = (EditText) findViewById(R.id.etOldPassword);

     }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (!(mOldPassword.getText().toString().equals(data.getString("pass","").toString()))) {
                    toast.makeText(this, "The old pass incorrect", Toast.LENGTH_SHORT).show();
                }
                else if ((mPassword.getText().toString().isEmpty() || mConfirmPassword.getText().toString().isEmpty())) {
                toast.makeText(this, "Can not save an Empty password", Toast.LENGTH_SHORT).show();
            }
                else if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                        SharedPreferences.Editor editor = data.edit();
                        editor.putString(getString(R.string.data_vname_pass),mPassword.getText().toString());
                        editor.putString(getString(R.string.data_vname_pass_hint),mPassHint.getText().toString());
                        editor.commit();
                        this.onBackPressed();
                }
                else {
                    toast.makeText(this, "There is a difference between the passwords", Toast.LENGTH_SHORT).show();
                    mConfirmPassword.setText("");
                }
                break;
            case R.id.btnClear:
                mPassword.setText("");
                mConfirmPassword.setText("");

                break;
            case R.id.btnCancel:
                this.onBackPressed();
                break;
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}

