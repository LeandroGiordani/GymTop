package com.example.gymtop.util

import android.content.Context
import androidx.startup.Initializer
import com.example.gymtop.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore

/**
 * FirestoreInitializer — conecta o Firestore ao emulador local em builds de debug.
 *
 * Porta padrão do Firestore emulator: 8080.
 */
class FirestoreInitializer : Initializer<FirebaseFirestore> {

    override fun create(context: Context): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        if (BuildConfig.DEBUG) {
            firestore.useEmulator(FIRESTORE_EMULATOR_HOST, FIRESTORE_EMULATOR_PORT)
        }
        return firestore
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    companion object {
        // The host '10.0.2.2' is a special IP address to let the
        // Android emulator connect to 'localhost'.
        private const val FIRESTORE_EMULATOR_HOST = "10.0.2.2"
        private const val FIRESTORE_EMULATOR_PORT = 8080
    }
}
