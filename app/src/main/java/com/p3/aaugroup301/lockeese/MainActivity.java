package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    byte[] cipherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button dattBtn = findViewById( R.id.button3 );
        dattBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(  );
                SimpleDateFormat dateFormat = new SimpleDateFormat(  " E dd-M-yyyy hh:mm:ss");
                Toast currentDate = Toast.makeText( getApplicationContext(), dateFormat.format( date ).toString(), Toast.LENGTH_SHORT  );
                currentDate.show();
            }
        });


    }
}
