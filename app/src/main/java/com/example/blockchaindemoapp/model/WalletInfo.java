package com.example.blockchaindemoapp.model;

import org.web3j.crypto.WalletFile;

public class WalletInfo {

    private WalletFile walletFile;
    private String walletMnemonic;

    public WalletFile getWalletFile() {
        return walletFile;
    }

    public void setWalletFile(WalletFile walletFile) {
        this.walletFile = walletFile;
    }

    public String getWalletMnemonic() {
        return walletMnemonic;
    }

    public void setWalletMnemonic(String walletMnemonic) {
        this.walletMnemonic = walletMnemonic;
    }
}
