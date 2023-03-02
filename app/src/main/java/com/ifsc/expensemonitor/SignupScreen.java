package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SignupScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
    }

//    protected void databaseTest(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Teste");
//        TextView valor = ((TextView)findViewById(R.id.valor));
//        TextView input = ((TextView)findViewById(R.id.TestText));
//        Button enviar = ((Button)findViewById(R.id.enviar));
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                valor.setText(value);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//
//        enviar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myRef.setValue(input.getEditableText().toString());
//                input.setText("");
//            }
//        });
//   }

}