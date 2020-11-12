package com.yinkin.yinelderservice.ui.messages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
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
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseACL;
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

import static android.content.Context.MODE_PRIVATE;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;
    private EditText editMessage;
    private EditText editReceiver;

    ListView feedListView;
    private boolean isOnline() {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public void resetMessageTable(){
        SQLiteDatabase yinDatabase = getActivity().openOrCreateDatabase("Users", MODE_PRIVATE, null);
        yinDatabase.execSQL("DROP TABLE IF EXISTS  messagestable");
    }
    public void storeMessageInSQLite(String message, String reciever, String sender){
        try {
            SQLiteDatabase yinDatabase = getActivity().openOrCreateDatabase("Users", MODE_PRIVATE, null);

            yinDatabase.execSQL("CREATE TABLE IF NOT EXISTS messagestable (message VARCHAR, reciever VARCHAR, sender VARCHAR)");
            yinDatabase.execSQL("INSERT OR REPLACE INTO messagestable (message, reciever , sender ) VALUES ('"
                    + message
                    + "', '" + reciever + "', '" + sender + "')");
            //Cursor sqlcursor = yinDatabase.rawQuery("SELECT * FROM messagestable", null);

//            int nameIndex = sqlcursor.getColumnIndex("message");
//            //int passwordIndex= sqlcursor.getColumnIndex("password");
//            sqlcursor.moveToFirst();
//            while (!sqlcursor.isAfterLast()) {
//                Log.i("username", sqlcursor.getString(nameIndex));
//                //Log.i("password",sqlcursor.getString(passwordIndex));
//                sqlcursor.moveToNext();
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getFeedsFromSQlite() {
        SQLiteDatabase yinDatabase = getActivity().openOrCreateDatabase("Users", MODE_PRIVATE, null);
        Cursor sqlcursor = yinDatabase.rawQuery("SELECT * FROM messagestable", null);

        int contentIndex = sqlcursor.getColumnIndex("message");
        int recieverIndex= sqlcursor.getColumnIndex("reciever");
        int senderIndex = sqlcursor.getColumnIndex("sender");
        List<Map<String, String>> tweetData = new ArrayList<Map<String, String>>();

        sqlcursor.moveToFirst();
        while (!sqlcursor.isAfterLast()) {
            Log.i("content", sqlcursor.getString(contentIndex));
            //Log.i("password",sqlcursor.getString(passwordIndex));
            String content = sqlcursor.getString(contentIndex);
            String sender = sqlcursor.getString(senderIndex);
            Map<String, String> tweetInfo = new HashMap<String, String>();
            tweetInfo.put("content", content );
            tweetInfo.put("sender", "by: " + sender );
            tweetData.add(tweetInfo);
            sqlcursor.moveToNext();
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), tweetData,
                android.R.layout.simple_list_item_2, new String[]{"content", "sender"}, new int[]{android.R.id.text1, android.R.id.text2});
        feedListView.setAdapter(simpleAdapter);
    }

    public void getFeeds(){
        try {
            resetMessageTable();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
            String arg = ParseUser.getCurrentUser().getUsername();
            query.whereEqualTo("receiver", arg);
            query.setLimit(10);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        List<Map<String, String>> tweetData = new ArrayList<Map<String, String>>();
                        for (ParseObject tweet : objects) {
                            Map<String, String> tweetInfo = new HashMap<String, String>();
                            tweetInfo.put("content", tweet.getString("body"));
                            tweetInfo.put("sender", "by: " + tweet.getString("sender"));
                            tweetData.add(tweetInfo);
                            storeMessageInSQLite(tweetInfo.get("content").toString(),
                                    ParseUser.getCurrentUser().getUsername(),
                                    tweetInfo.get("sender").toString());
                        }
                        Log.i("Message data ", objects.size() + "");
                        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), tweetData,
                                android.R.layout.simple_list_item_2, new String[]{"content", "sender"}, new int[]{android.R.id.text1, android.R.id.text2});
                        feedListView.setAdapter(simpleAdapter);
                    }

                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
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




                // Get the layout inflater
                final LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView  = inflater.inflate(R.layout.dialog_send_message, null);
                alertDialog.setView(dialogView);
                editMessage = dialogView.findViewById(R.id.send_message);
                editReceiver = dialogView.findViewById(R.id.receiver);
                alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ParseObject tweet = new ParseObject("Messages");
                            tweet.put("sender", ParseUser.getCurrentUser().getUsername());
                            tweet.put("receiver", editReceiver.getText().toString());
                            tweet.put("body", editMessage.getText().toString());
                            ParseACL parseACL = new ParseACL();
                            parseACL.setPublicReadAccess(true);
                            tweet.setACL(parseACL);
                            tweet.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Message send", "successful");
                                        Toast.makeText(getContext(), "Message sended", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.i("Message ", "fail");
                                        Toast.makeText(getContext(), "Message fail to send", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }catch (Exception e){
                            Log.i("error", e.getMessage());
                            Toast.makeText(getContext(), "Message fail to send because of an error", Toast.LENGTH_SHORT).show();
                        }
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
        //SwitchPreferenceCompat mSwitchPreference;
        SharedPreferences devicePreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isSync = devicePreference.getBoolean("sync", false);
        if(!isSync || !isOnline()) {
            getFeedsFromSQlite();
        } else{
            getFeeds();
        }
        return root;
    }
}