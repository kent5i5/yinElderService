package com.yinkin.yinelderservice.ui.messages;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yinkin.yinelderservice.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;

    ListView feedListView;

    public void getFeeds(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        String arg = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("receiver", arg );
        query.setLimit(5);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<Map<String, String>> tweetData = new ArrayList<Map<String, String>>();
                for (ParseObject tweet : objects){
                    Map<String, String> tweetInfo = new HashMap<String, String>();
                    tweetInfo.put("content", tweet.getString("body"));
                    tweetInfo.put("sender", "by: "+tweet.getString("sender"));
                    tweetData.add(tweetInfo);
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), tweetData,
                        android.R.layout.simple_list_item_2, new String[]{"content","sender"}, new int[] {android.R.id.text1, android.R.id.text2});
                feedListView.setAdapter(simpleAdapter);
            }
        });
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel =
                new ViewModelProvider(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("New Tweet");
                final EditText editTweet = new EditText(getContext());
                final EditText editReciver = new EditText(getContext());
                alertDialog.setView(editTweet);
                //alertDialog.setView(editReciver);
                alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Info",editTweet.getText().toString());
                        ParseObject tweet = new ParseObject("Messages");
                        tweet.put("sender",ParseUser.getCurrentUser().getUsername());
                        tweet.put("receiver", "ocean1");
                        tweet.put("body",editTweet.getText().toString());
                        tweet.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("Parse tweet", "successful");
                                    Toast.makeText(getContext(),"Message sended",Toast.LENGTH_SHORT).show();
                                } else{
                                    Log.i("Parse tweet", "fail");
                                    Toast.makeText(getContext(),"Message fail to send",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        feedListView = root.findViewById(R.id.tweetListView);
        final TextView textView = root.findViewById(R.id.text_messages);
        messagesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        getFeeds();
        return root;
    }
}