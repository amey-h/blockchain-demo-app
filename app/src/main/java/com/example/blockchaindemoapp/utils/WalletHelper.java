package com.example.blockchaindemoapp.utils;

import android.content.Context;
import android.util.Log;

import com.example.blockchaindemoapp.model.WalletInfo;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WalletHelper {

    private static int HARDENED_BIT = -2147483648;
    private static String fileExt = ".json";
    private Context context;
    private static final String TAG = WalletHelper.class.getSimpleName();
    private File cacheDir, file;

    public WalletHelper(Context c) {
        this.context = c;
    }

    public WalletInfo createWallet(String password) {
        WalletInfo walletInfo = new WalletInfo();
        try {
            String walletMnemonic = generateMnemonic();
            WalletFile walletFile = generateWallet(password, walletMnemonic);
            walletInfo.setWalletFile(walletFile);
            walletInfo.setWalletMnemonic(walletMnemonic);
        } catch (CipherException ce) {
            ce.printStackTrace();
        }
        return walletInfo;
    }

    private String generateMnemonic() {
        byte[] initialEntropy = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(initialEntropy);
        String walletMnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        Log.d(TAG, "walletMnemonic: " + walletMnemonic);
        return walletMnemonic;
    }

    private WalletFile generateWallet(String password, String walletMnemonic) throws CipherException {
        byte[] seed = MnemonicUtils.generateSeed(walletMnemonic, password);
        Bip32ECKeyPair bip32ECKeyPair = Bip32ECKeyPair.generateKeyPair(seed);
        int[] path = new int[]{44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, 0};
        Bip32ECKeyPair derived = Bip32ECKeyPair.deriveKeyPair(bip32ECKeyPair, path);
        Credentials credentials = Credentials.create(derived);
        WalletFile walletFile = Wallet.createLight(password, credentials.getEcKeyPair());

        generateFileName(walletFile);

        return walletFile;
    }

    private void generateFileName(WalletFile walletFile) {
        String fileName = "";
        try {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
            Date date = new Date();
            String dateStr = df.format(date);

            fileName = walletFile.getAddress() + "_" + dateStr + fileExt;
            Log.d(TAG, "Filename: " + fileName);

            cacheDir = context.getExternalCacheDir();
            file = new File(cacheDir + "/" + fileName);
            Log.d(TAG, "Filepath: " + file.getAbsolutePath());

            if (!file.exists()) {
                file.createNewFile();
                Log.d(TAG, "File created");
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(file, walletFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        PreferenceUtils.getInstance(context).saveWalletFileName(fileName);
    }
}
