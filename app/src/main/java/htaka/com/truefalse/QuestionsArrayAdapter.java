package htaka.com.truefalse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 9/4/16.
 */
public class QuestionsArrayAdapter extends ArrayAdapter {

    final String TAG="TestingShit";
    private final Context context;
    List<HT_Item> values=new ArrayList<HT_Item>();
    String counter;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public QuestionsArrayAdapter(Context context, List <HT_Item> values) {
        super(context, R.layout.list_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_layout, parent, false);
        final TextView titleTxt= (TextView) rowView.findViewById(R.id.nameTextItem);
        final TextView scoreTxt= (TextView) rowView.findViewById(R.id.scoreTextItem);

        Log.i(TAG ,""+values.get(position).getUsername() + "Working Pro" );

        titleTxt.setText(values.get(position).getUsername());
        scoreTxt.setText(values.get(position).getUserscore());

        return rowView;
    }
}
