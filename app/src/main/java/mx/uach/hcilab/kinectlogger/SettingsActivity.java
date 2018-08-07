package mx.uach.hcilab.kinectlogger;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_NAME = "GAMES_PREFERENCES";

    private static final String TAG_STATUS_DELAY = "STATUS_DELAY_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(
                SHARED_PREFERENCES_NAME,
                MODE_PRIVATE
        );

        double statusDelayTime = sharedPreferences.getFloat(TAG_STATUS_DELAY, 1);

        EditText textStatusDelayTime = findViewById(R.id.text_settings_status_delay_time);
        textStatusDelayTime.setText(String.valueOf(statusDelayTime));

        textStatusDelayTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    SharedPreferences.Editor editor = getSharedPreferences(
                            SHARED_PREFERENCES_NAME,
                            MODE_PRIVATE
                    ).edit();

                    editor.putFloat(TAG_STATUS_DELAY, (float) Double.parseDouble(editable.toString()));
                    editor.apply();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
