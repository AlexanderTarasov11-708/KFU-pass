package com.itis.kfupass

import android.bluetooth.*
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult


class BluetoothConnection: AppCompatActivity() {

    var REQUEST_ENABLE_BT = 1

    fun smth() {
        val bluetooth = BluetoothAdapter.getDefaultAdapter()

        var status = "Bluetooth выключен"
        if(bluetooth!=null)
        {
            if (bluetooth.isEnabled) { // Bluetooth включен. Работаем.
                var myDeviceAddress = bluetooth.address
                var myDeviceName = bluetooth.name
                status = "$myDeviceName : $myDeviceAddress"
            }
            else { // Bluetooth выключен. Предложим пользователю включить его.
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }
        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
    }

}