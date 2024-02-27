package com.swidch.otacauth.View.component.Dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.swidch.otacauth.R

class QRAlertDialog(context: Context, value: String) : Dialog(context) {

    private var otacValue: String? = value
    private lateinit var negativeButton: Button
    private lateinit var qrCodeImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_qr_alert)
        setCanceledOnTouchOutside(false)

        val dm = context.applicationContext.resources.displayMetrics
        val layoutParams = window?.attributes
        layoutParams?.width = dm.widthPixels
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        qrCodeImage = findViewById(R.id.qr_code_image2)
        generateQRCode(otacValue)
        negativeButton = findViewById(R.id.negative_button)
        negativeButton.setOnClickListener{ dismiss() }
    }

    private fun generateQRCode(value: String?):Boolean {
        val qrCode = QRCodeWriter()
        val bitMatrix = qrCode.encode(value, BarcodeFormat.QR_CODE, 120, 120)
        val bitmap: Bitmap = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
        for (i in 0 until bitMatrix.width) {
            for (j in 0 until bitMatrix.height) {
                val color = if (bitMatrix.get(i,j)) {
                    Color.BLACK
                } else {
                    context.getColor(R.color.qr_code_background)
                }
                bitmap.setPixel(i, j, color)
            }
        }
        qrCodeImage.setImageBitmap(bitmap)
        return true
    }
}