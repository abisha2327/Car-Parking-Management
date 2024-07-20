package com.example.park;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

public class booking_details extends AppCompatActivity {
    Button button;

    TextView timer1,timer2;
    int t1Hour, t1Minute, t2Hour, t2Minute;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            // Handle the case where the user is not logged in
            return;
        }
        String userId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Bookings").child(userId);

        button = findViewById(R.id.button8);
        timer1 = findViewById(R.id.textView9);
        timer2 = findViewById(R.id.textView10);

        timer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        booking_details.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,int hourOfDay,int minute) {

                                t1Hour = hourOfDay;
                                t1Minute = minute;

                                Calendar calendar = Calendar.getInstance();

                                calendar.set(0,0,0,t1Hour,t1Minute);

                                timer1.setText(DateFormat.format( "hh:mm aa",calendar));

                            }
                        },12,0,false

                );
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();
            }
        });

        timer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        booking_details.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                t1Hour = hourOfDay;
                                t1Minute = minute;

                                Calendar calendar = Calendar.getInstance();

                                calendar.set(0, 0, 0, t1Hour, t1Minute);

                                timer2.setText(DateFormat.format("hh:mm aa", calendar));

                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeBookingDetails();
            }
        });
    }
    private void storeBookingDetails() {
        String time1 = timer1.getText().toString();
        String time2 = timer2.getText().toString();

        if (time1.isEmpty() || time2.isEmpty()) {
            Toast.makeText(booking_details.this, "Please select both times", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(Calendar.getInstance().getTime());

        // Create a Map to store the booking details
        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("time1", time1);
        bookingDetails.put("time2", time2);
        bookingDetails.put("date", currentDate);

        // Store the booking details in Firebase Database
        reference.push().setValue(bookingDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(booking_details.this, "Booking details stored successfully", Toast.LENGTH_SHORT).show();
                            // Start the next activity
                            Intent i = new Intent(booking_details.this, slot_Activity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(booking_details.this, "Failed to store booking details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}