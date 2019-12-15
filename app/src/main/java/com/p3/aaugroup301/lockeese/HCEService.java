package com.p3.aaugroup301.lockeese;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

//sending LockKey to NFC reader
public class HCEService extends HostApduService {

    String AID = "A0000002471001";

    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {

        Log.e("NFC","success");
        if (Arrays.copyOfRange(bytes,10,24) == AID.getBytes())  {
            return KeysListActivity.getListOfKeys().get(1).getKeyHash().getBytes();
        } else {
            return "Error".getBytes();
        }



        //Log.d("NFCTest", "message from reader: " + bytes);
        // all communication should be encrypted
       //a) get request from NFC reader to send LockKey -> send the LockKey
        // b) LockKey is wrong -> mess 'the LockKey is incorrect'
        // c) the was an error -> resend the LockKey
    }

    @Override
    public void onDeactivated(int i) {


    }
}
