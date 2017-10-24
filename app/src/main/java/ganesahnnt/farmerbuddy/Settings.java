package ganesahnnt.farmerbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends CustomActivityWithMenu {

    private static Button editMail;
    private static Button editPersonalInfo;
    private static Button editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editMail = (Button) findViewById(R.id.edit_mail);
        editMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, EditEmail.class));
            }
        });
        editPass = (Button) findViewById(R.id.edit_pass);
        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, EditPassword.class));
            }
        });
        editPersonalInfo = (Button) findViewById(R.id.edit_personal_info);
        editPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, EditPersonalInfo.class));
            }
        });
    }

}
