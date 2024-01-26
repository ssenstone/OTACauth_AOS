package com.swidch.otacauth.View.component.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.swidch.otacauth.R

class CMAlertDialog: Dialog {
    private var message: String? = null
    private var positiveButtonMessage: String? = null
    private var negativeButtonMessage: String? = null
    private var classification: String? = null
    private var positiveButtonClickListener: View.OnClickListener? = null
    private var negativeButtonClickListener: View.OnClickListener? = null

    private lateinit var messageText: TextView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    constructor(context: Context, classification: String, message: String?,buttonMessage: String?, positiveButtonClickListener: View.OnClickListener) : super(context) {
        this.message = message
        this.positiveButtonMessage = buttonMessage
        this.classification = classification
        this.positiveButtonClickListener = positiveButtonClickListener
    }
    constructor(context: Context, classification: String, message: String?, positiveButtonMessage: String?, negativeButtonMessage: String?, positiveButtonClickListener: View.OnClickListener, negativeButtonClickListener: View.OnClickListener) : super(context) {
        this.message = message
        this.classification = classification
        this.positiveButtonMessage = positiveButtonMessage
        this.negativeButtonMessage = negativeButtonMessage
        this.positiveButtonClickListener = positiveButtonClickListener
        this.negativeButtonClickListener = negativeButtonClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_cm_alert)
        setCanceledOnTouchOutside(false)

        val dm = context.applicationContext.resources.displayMetrics
        val layoutParams = window?.attributes
        layoutParams?.width = dm.widthPixels
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        messageText = findViewById(R.id.cm_alert_message_text)
        positiveButton = findViewById(R.id.positive_button)
        negativeButton = findViewById(R.id.negative_button)

        messageText.text = message
        positiveButton.setText(positiveButtonMessage)
        negativeButton.setText(negativeButtonMessage)

        when(classification) {
            SIMPLE_POSITIVE_ALERT -> {
                negativeButton.visibility = View.GONE
            }

            SIMPLE_NEGATIVE_POSITIVE_ALERT -> {
                negativeButton.visibility = View.VISIBLE
            }
        }

        negativeButton.setOnClickListener (negativeButtonClickListener)


        positiveButton.setOnClickListener (positiveButtonClickListener)
    }

    companion object {
        const val SIMPLE_POSITIVE_ALERT = "SIMPLE_POSITIVE_ALERT"
        const val SIMPLE_NEGATIVE_POSITIVE_ALERT = "SIMPLE_NEGATIVE_POSITIVE_ALERT"
    }
}