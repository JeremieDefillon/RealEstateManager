package com.openclassrooms.realestatemanager;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class BugsFixedMainActivity extends AppCompatActivity {

        private TextView textViewMain;
        private TextView textViewQuantity;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // 1st ERROR => this textViewMain appear null into debugger (the resources id isn't correct)
            // this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
            // FIXED VERSION
            this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
            this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

            this.configureTextViewMain();
            this.configureTextViewQuantity();
        }

        private void configureTextViewMain(){
            this.textViewMain.setTextSize(15);
            this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
        }

        private void configureTextViewQuantity(){
            int quantity = Utils.convertDollarToEuro(100);
            this.textViewQuantity.setTextSize(20);
            // 2nd ERROR => Quantity is expected as a string and not an int
            //this.textViewQuantity.setText(quantity);
            // FIXED VERSION using "String.valueOf(quantity)"
            this.textViewQuantity.setText(String.valueOf(quantity));
        }
}
