package htaka.com.truefalse;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends ListActivity {

    TextView scoreTxt;
    EditText usernameEdiTxt;
    Button addBtn;

    String TAG="Firebase Info";
    int score,perPage=10;
    DatabaseReference mDatabase;
    List<HT_Item> items=new ArrayList<HT_Item>();
    List<DataSnapshot> dataList=new ArrayList<DataSnapshot>();

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        storage = FirebaseStorage.getInstance();

        scoreTxt = (TextView)findViewById(R.id.resultTextView);
        usernameEdiTxt = (EditText) findViewById(R.id.editText);

        score = getIntent().getIntExtra("Score", score);
        scoreTxt.setText(String.valueOf(score));

        SignIn();
        SetFireBase();

        addBtn= (Button) findViewById(R.id.addScoreButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToFirebase();
            }
        });
    }

    private void SignIn() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("NOTHING", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("NOTHING", "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Shit", "signInAnonymously:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Shit", "signInAnonymously", task.getException());
                            Toast.makeText(ResultsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SetFireBase() {
        items.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Scores").orderByValue().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataList = Lists.newArrayList(dataSnapshot.getChildren());
                        long  dataSize=dataSnapshot.getChildrenCount();
                        Log.w(TAG, ""+dataSize);
                        if(perPage>dataSize)
                            perPage=(int)dataSize;
                        for(int i=perPage-1;i>=0;i--) {
                            String scr=" ",nme=" ";

                            if(dataList.get(i).getKey().toString() != null)
                                nme = dataList.get(i).getKey().toString();
                            if(dataList.get(i).getValue().toString()!=null)
                                scr =dataList.get(i).getValue().toString();

                            Log.i(TAG ,""+dataList.get(i).getKey());
                            items.add(new HT_Item(nme,scr));
                        }
                        setListAdapter(new QuestionsArrayAdapter(getApplicationContext(), items));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    private void AddToFirebase() {
        boolean exists=false;

        String from=usernameEdiTxt.getText().toString();
        String msg=" ";

        for(int i=0;i<dataList.size();i++) {
            if(dataList.get(i).getKey().toString().equals(from)){
                exists=true;
            }
            Log.w(TAG,dataList.get(i).getKey().toString());
        }

        if(score==0){
            msg = "نتيجة غير جيدة";
        } else if(from.trim().length() <= 2){
            msg = "الرجاء كتابة اسم مكون من ٣ احرف";
        } else if (exists) {
            msg = "هذا الاسم مستعمل";
        } else{
            mDatabase.child("Scores").child(from).setValue(score);
            msg = "تم الارسال";
        }

        Toast.makeText(ResultsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
