package ganesahnnt.farmerbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Search extends CustomActivityWithMenu {

    private static Button searchButton;
    private static EditText textToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textToSearch = (EditText) findViewById(R.id.search_menuText_search);
        searchButton = (Button) findViewById(R.id.search_button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = textToSearch.getText().toString();
                Intent intent = new Intent(Search.this, SearchResult.class);
                intent.putExtra("wordToSearch", word);
                startActivity(intent);
            }
        });
    }

}
