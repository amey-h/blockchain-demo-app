package com.example.blockchaindemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blockchaindemoapp.model.WalletInfo;
import com.example.blockchaindemoapp.utils.PreferenceUtils;
import com.example.blockchaindemoapp.utils.WalletHelper;
import com.kenai.jffi.Main;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText passwordEditText;
    private Button createWalletButton;
    private String serviceUrl = "http://127.0.0.1:7545";
    private Web3j web3j;
    private WalletInfo walletInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordEditText = (EditText) findViewById(R.id.edit_password);
        createWalletButton = (Button) findViewById(R.id.btn_wallet);

        web3j = Web3j.build(new HttpService(serviceUrl));

        createWalletButton.setOnClickListener(view -> {

            if (!TextUtils.isEmpty(passwordEditText.getText().toString())) {

                WalletHelper walletHelper = new WalletHelper(MainActivity.this);
                walletInfo = walletHelper.createWallet(passwordEditText.getText().toString());
                Log.d(TAG, "walletAddress: " + walletInfo.getWalletFile().getAddress());
                if (!TextUtils.isEmpty(walletInfo.getWalletFile().getAddress())) {
                    Toast.makeText(MainActivity.this, "Wallet create", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
