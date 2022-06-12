package appdev.sapir.maor.passkeeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DisableEntryPasswordActivity extends AppCompatActivity {

    private EditText mConfirmPassword;
    private SharedPreferences data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_entry_password);
        mConfirmPassword = (EditText) findViewById(R.id.etpConfirmPassword);
        data = getSharedPreferences("Pass_Keeper_data", Context.MODE_PRIVATE);
     }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (mConfirmPassword.getText().toString().equals(data.getString("pass",""))) {
                        SharedPreferences.Editor editor = data.edit();
                        editor.putBoolean("entryPass",false);
                        editor.commit();
                        this.onBackPressed();
                }
                else {
                    Toast.makeText(this,"Incorrect passwords",Toast.LENGTH_LONG).show();
                    mConfirmPassword.setText("");
                }
                break;
            case R.id.btnClear:
                mConfirmPassword.setText("");
                break;
            case R.id.btnCancel:
                this.onBackPressed();
                break;
        }
    }
}

