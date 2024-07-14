package com.example.asnova.ui.theme

import android.annotation.SuppressLint
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.asnova.R

@SuppressLint("ResourceType")
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.xml.com_google_android_gms_fonts_certs
)

val inter = GoogleFont("Inter")


val fontFamilyInter = FontFamily(
    Font(
        googleFont = inter,
        fontProvider = provider,
    )
)