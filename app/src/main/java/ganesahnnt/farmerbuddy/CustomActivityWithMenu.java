package ganesahnnt.farmerbuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import model.UserSessionManager;

@SuppressLint("Registered")
public class CustomActivityWithMenu extends AppCompatActivity {

    protected UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSessionManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        MenuManager manager = new MenuManager();
        manager.setMenuButtons(menu, this.getClass(), session);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuManager manager = new MenuManager();

        Class goingToClass = manager.onItemClick(item, session);

        if (goingToClass == null)
            session.logoutUser();
        else if (goingToClass == ViewUser.class) {
            Intent intent = new Intent(this, ViewUser.class);
            intent.putExtra("id", Long.parseLong(session.getUserDetails().get(UserSessionManager.KEY_ID)));
            startActivity(intent);
        } else
            startActivity(new Intent(this, goingToClass));

        return true;
    }
}
