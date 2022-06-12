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
public class AddEntryPasswordActivity extends AppCompatActivity {

    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mPassHint;
    private SharedPreferences data;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = getSharedPreferences(getString(R.string.data_uri_name), Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry_password);
        mPassword = (EditText) findViewById(R.id.etpEnterPassword);
        mConfirmPassword = (EditText) findViewById(R.id.etpConfirmPassword);
        mPassHint = (EditText) findViewById(R.id.et_pass_hint);

     }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if ((mPassword.getText().toString().isEmpty() || mConfirmPassword.getText().toString().isEmpty())) {
                toast.makeText(this, getString(R.string.err_msg_empty_pass), Toast.LENGTH_SHORT).show();
            }
                else if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                        SharedPreferences.Editor editor = data.edit();
                        editor.putString(getString(R.string.data_vname_pass),mPassword.getText().toString());
                        editor.putString(getString(R.string.data_vname_pass_hint),mPassHint.getText().toString());
                        editor.putBoolean(getString(R.string.data_vname_have_entry_pass),true);
                        editor.commit();
                        this.onBackPressed();
                }
                else {
                    toast.makeText(this, getString(R.string.err_msg_two_pass_diff), Toast.LENGTH_SHORT).show();
                    mConfirmPassword.setText("");
                }
                break;
            case R.id.btnClear:
                mPassword.setText("");
                mConfirmPassword.setText("");
                mPassHint.setText("");
                break;
            case R.id.btnCancel:
                this.onBackPressed();
                break;
        }
    }

}

