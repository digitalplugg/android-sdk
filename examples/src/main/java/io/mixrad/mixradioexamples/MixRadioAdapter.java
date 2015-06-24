package io.mixrad.mixradioexamples;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.MusicItem;
import io.mixrad.mixradiosdk.model.Product;

/**
 * Created by mattaranha on 19/06/15.
 */
public class MixRadioAdapter extends ArrayAdapter<Object>
{
    private final Context context;
    private ArrayList<Object> data = null;

    public MixRadioAdapter(Context context, int textViewResourceId, ArrayList<Object> data)
    {
        super(context, textViewResourceId, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public void addAll(Collection<?> collection) {
        Log.d("FRAG", "adding collection " + collection.size());
        super.addAll(collection);
    }

    @Override
    public int getCount() {
        Log.d("FRAG", "get size " + super.getCount() + " " + data.size());
        return super.getCount();
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public Object getItem (int position) {
        return this.data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView text1 = (TextView)rowView.findViewById(R.id.label);
        TextView text2 = (TextView)rowView.findViewById(R.id.subLabel);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.icon);

        // check types of the objects
        Object o = data.get(position);
        if(o.getClass().equals(String.class))
        {   text1.setText(o.toString());
            text2.setText("");
        }
        else if(o.getClass().equals(Artist.class))
        {   Artist a = (Artist)o;
            text1.setText(a.name);
            text2.setText("");
            if(a.thumb100Uri != null)
            {   Picasso.with(context).load(a.thumb100Uri).into(imageView);
            }
        }
        else if(o.getClass().equals(Product.class))
        {   Product p = (Product)o;
            List<Artist> as = p.performers;
            text1.setText(p.name);

            if(as != null)
            {
                Artist a = as.get(0);
                text2.setText(a.name);
            }


            if(p.thumb100Uri != null)
            {   Picasso.with(context).load(p.thumb100Uri).into(imageView);
            }
            else
            {   imageView.setImageResource(android.R.color.transparent);
            }
        }
        else if(o.getClass().equals(Genre.class))
        {   Genre g = (Genre)o;
            text1.setText(g.name);
            text2.setText("");
        }
        else if(o instanceof MusicItem)
        {   text1.setText("MusicItem");
            text2.setText(((MusicItem)o).name);
        }
        return rowView;
    }

}
