package com.phl.shubham

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var camera_permission = 200
    var btn_onoff: Button? = null
    var off_image: ImageView? = null
    private var checkflash: Boolean? = false
    var cameraManager: CameraManager? = null
    private var parameters: Camera.Parameters? = null
    private var isflashlighton: Boolean? = false
    private var manager: CameraManager? = null
    var cameraid: String? = null
    private var permissions: Int? = null
    private var blink1: Button? = null
    private var countDownTimer: CountDownTimer? = null
    private var temp:Long=0
    private var ison:Boolean=true
    private var blink2:Button?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialise()

    }

    private fun initialise() {


        off_image = findViewById(R.id.torch_image) as ImageView
        btn_onoff = findViewById(R.id.btn_onoff)
        blink1 = findViewById(R.id.blink1) as Button
        blink2=findViewById(R.id.blink2) as Button
        checkflash = application.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        permissions = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        btn_onoff!!.setOnClickListener {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (permissions != PackageManager.PERMISSION_GRANTED) {
                        setup_permissions()
                    } else {
                        openflashlight()

                    }

                } else {
                    openflashlight()
                }
        }

        blink1!!.setOnClickListener {
            if (ison == true) {
                blink1!!.setBackgroundResource(R.drawable.stroke_blink1)
                blink1!!.setText("STOP")
                ison=false
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (permissions != PackageManager.PERMISSION_GRANTED) {
                        setup_permissions()

                    } else {
                        sos()

                    }

                } else {
                    onStop()
                    sos()

                }

            }
            else{
                blink1!!.setBackgroundResource(R.drawable.style_blink1)
                blink1!!.setText("SOS")
                countDownTimer!!.cancel()
                cameraManager!!.setTorchMode(cameraid!!,false)
                ison=true
                off_image!!.setImageResource(R.drawable.lightoff)
            }
        }

        blink2!!.setOnClickListener {
            if (ison == true) {
                blink2!!.setBackgroundResource(R.drawable.stroke_blink2)
                blink2!!.setText("STOP")
                ison=false
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (permissions != PackageManager.PERMISSION_GRANTED) {
                        setup_permissions()

                    } else {
                        sos2()

                    }

                } else {
                    onStop()
                    sos2()

                }
            }
            else{
                blink2!!.setBackgroundResource(R.drawable.style_blink2)
                blink2!!.setText("BLINK")
                countDownTimer!!.cancel()
                cameraManager!!.setTorchMode(cameraid!!,false)
                ison=true
                off_image!!.setImageResource(R.drawable.lightoff)
            }
        }

    }

    private fun setup_permissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            camera_permission
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            camera_permission -> {
                if (grantResults.isEmpty() || !grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permissions Denied", Toast.LENGTH_LONG).show()
                } else {
                    openflashlight()
                }
            }
        }
    }

    private fun openflashlight() {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraid = cameraManager!!.cameraIdList[0]
        if (!isflashlighton!!) {
            cameraManager!!.setTorchMode(cameraid!!, true)
            off_image!!.setImageResource(R.drawable.light_on)
            btn_onoff!!.setBackgroundResource(R.drawable.offbtn_rounded)
            btn_onoff!!.setText("OFF")
            isflashlighton = true
        } else {
            cameraManager!!.setTorchMode(cameraid!!, false)
            off_image!!.setImageResource(R.drawable.lightoff)
            btn_onoff!!.setBackgroundResource(R.drawable.rounded_btnoff)
            isflashlighton = false
            btn_onoff!!.setText("ON")

        }
    }

    private fun sos() {

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraid = cameraManager!!.cameraIdList[0]

                countDownTimer = object : CountDownTimer(10000000, 150) {


                    override fun onTick(p0: Long) {
                        off_image!!.setImageResource(R.drawable.light_on)
                        if(p0%2==temp) {
                            cameraManager!!.setTorchMode(cameraid!!, true)
                        }
                        else{
                            cameraManager!!.setTorchMode(cameraid!!, false)

                        }
                    }

                    override fun onFinish() {
                        blink1!!.setText("SOS")
                        blink1!!.setBackgroundResource(R.drawable.style_blink1)
                        off_image!!.setImageResource(R.drawable.lightoff)
                        cameraManager!!.setTorchMode(cameraid!!, false)

                    }

                }.start()

    }

    private fun sos2() {

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraid = cameraManager!!.cameraIdList[0]

        countDownTimer = object : CountDownTimer(10000000, 100) {

            override fun onTick(p0: Long) {
                off_image!!.setImageResource(R.drawable.light_on)
                if(p0%2==temp) {
                    cameraManager!!.setTorchMode(cameraid!!, true)
                }
                else{
                    cameraManager!!.setTorchMode(cameraid!!, false)
                }

            }

            override fun onFinish() {
                off_image!!.setImageResource(R.drawable.lightoff)
                blink2!!.setBackgroundResource(R.drawable.style_blink1)
                blink2!!.setText("BLINK")
                cameraManager!!.setTorchMode(cameraid!!, false)

            }

        }.start()

    }

}





