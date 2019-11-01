package com.p3.aaugroup301.lockeese;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

//sending LockKey to NFC reader
public class HCEService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {


        // all communication should be encrypted
       //a) get request from NFC reader to send LockKey -> send the LockKey
        // b) LockKey is wrong -> mess 'the LockKey is incorrect'
        // c) the was an error -> resend the LockKey

        return new byte[0];
    }

    @Override
    public void onDeactivated(int i) {

    }
}
