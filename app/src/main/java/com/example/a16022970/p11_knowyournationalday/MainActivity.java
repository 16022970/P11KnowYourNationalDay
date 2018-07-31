package com.example.a16022970.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] list;
    String msg = "";
    SessionManager session;
    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(MainActivity.this);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        lv = (ListView) findViewById(R.id.lv);

        list = new String[]{"Singapore National Day is on 9 Aug", "Singapore is 53 years old",
                "Theme is We Are Singapore"};

        for (int i = 0; i < list.length; i++) {
            msg += list[i] + "\n";
        }
        session.checkLogin();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
        lv.setAdapter(adapter);

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase =
                (LinearLayout) inflater.inflate(R.layout.passphrase, null);
        final EditText etPassphrase = (EditText) passPhrase
                .findViewById(R.id.editTextPassPhrase);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please login")
                .setView(passPhrase)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (etPassphrase.getText().toString().equals("738964")) {
                            session.createLoginSession(etPassphrase.getText().toString());
                            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();
                        } else {
                            Intent i = new Intent(MainActivity.this, NoAccessCode.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Please obtain access code in order to be able to access app.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("No Access code", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(MainActivity.this, NoAccessCode.class);
                                startActivity(i);
                                Toast.makeText(MainActivity.this, "No access code", Toast.LENGTH_LONG).show();

                            }
                        }
                );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.sendFriend) {
            String[] listSend = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(listSend, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                // Put essentials like email address, subject & body text
                                email.putExtra(Intent.EXTRA_SUBJECT,
                                        "Know your National Day");
                                email.putExtra(Intent.EXTRA_TEXT, msg);
                                // This MIME type indicates email
                                email.setType("message/rfc822");
                                // createChooser shows user a list of app that can handle
                                // this MIME type, which is, email
                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));

                                Toast.makeText(MainActivity.this, "You chose Email",
                                        Toast.LENGTH_LONG).show();
                            } else {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "", null)));
                                sendSMS(lv);
                                Toast.makeText(MainActivity.this, "You chose SMS",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.quiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            final RadioGroup rg1 = (RadioGroup)passPhrase.findViewById(R.id.rg1);
            final RadioGroup rg2 = (RadioGroup)passPhrase.findViewById(R.id.rg2);
            final RadioGroup rg3= (RadioGroup)passPhrase.findViewById(R.id.rg3);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Enter")
                    .setView(passPhrase)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            int ans1 = rg1.getCheckedRadioButtonId();
                            int ans2 = rg2.getCheckedRadioButtonId();
                            int ans3 = rg3.getCheckedRadioButtonId();

                            RadioButton rb1 = (RadioButton)passPhrase.findViewById(ans1);
                            RadioButton rb2 = (RadioButton)passPhrase.findViewById(ans2);
                            RadioButton rb3 = (RadioButton)passPhrase.findViewById(ans3);

                            if(rb1.getText().toString().equals("No")){
                                score += 1;
                            }
                            if(rb2.getText().toString().equals("Yes")){
                                score += 1;
                            }
                            if(rb3.getText().toString().equals("Yes")){
                                score += 1;
                            }

                           Snackbar.make(lv, "Score: " + score + "/3", Snackbar.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton("Don't know lah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(MainActivity.this, "You gave up",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            session.logoutUser();
//                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            Toast.makeText(MainActivity.this, "quit",
//                                Toast.LENGTH_LONG).show();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(MainActivity.this, "Not quit",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    public void sendSMS(View v) {
        Uri uri = Uri.parse("smsto:");
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", msg);
        startActivity(it);
    }
}
