package ganesahnnt.farmerbuddy.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ganesahnnt.farmerbuddy.R;

public class CategoryAdapter extends ArrayAdapter<String> {

    private Activity context;
    private List<String> categories;

    public CategoryAdapter(Activity context, List<String> categories) {
        super(context, R.layout.category_view, categories);
        this.context = context;
        this.categories = categories;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.category_view, parent, false);
        }

        String category = categories.get(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.catImage);
        TextView name = (TextView) convertView.findViewById(R.id.catName);

        switch (category) {

            case "Vegetables":
                image.setImageResource(R.drawable.electronic);
                break;
            case "Dairy Products":
                image.setImageResource(R.drawable.book);
                break;
            case "Seeds":
                image.setImageResource(R.drawable.clothes);
                break;
            case "Cereals & Pulses":
                image.setImageResource(R.drawable.furniture);
                break;
            case "Flowers":
                image.setImageResource(R.drawable.accessories);
                break;
            case "Oil Varieties":
                image.setImageResource(R.drawable.sport);
                break;
            case "Fertilizers":
                image.setImageResource(R.drawable.shoes);
                break;
            case "Green Leafy":
                image.setImageResource(R.drawable.animals);
                break;
            case "Cattle":
                image.setImageResource(R.drawable.cosmetics);
                break;
            case "Fruits":
                image.setImageResource(R.drawable.music);
                break;
            case "Machines & Instruments":
                image.setImageResource(R.drawable.car);
                break;


        }
        name.setText(category);

        return convertView;
    }
}
