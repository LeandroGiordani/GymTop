package com.example.gymtop.util

import android.content.Context
import androidx.startup.Initializer
import com.example.gymtop.BuildConfig
import com.google.firebase.auth.FirebaseAuth

/**
 * AuthInitializer — conecta o Firebase Auth ao emulador local em builds de debug.
 *
 * Roda automaticamente via Jetpack App Startup antes do Application.onCreate().
 * Em builds de release, esta classe não existe — o source set debug é excluído.
 *
 * Emulator host: 10.0.2.2 é o alias do localhost do computador no emulador Android.
 * Porta padrão do Auth emulator: 9099.
 */
class AuthInitializer : Initializer<FirebaseAuth> {

    override fun create(context: Context): FirebaseAuth {
        val auth = FirebaseAuth.getInstance()
        // useEmulator só pode ser chamado uma vez; protege contra reinicialização
        if (BuildConfig.DEBUG) {
            auth.useEmulator(FIRESTORE_EMULATOR_HOST, FIRESTORE_EMULATOR_PORT)
        }
        return auth
    }

    // Sem dependências de outros Initializers
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    companion object {
        // The host '10.0.2.2' is a special IP address to let the
        // Android emulator connect to 'localhost'.
        private const val FIRESTORE_EMULATOR_HOST = "10.0.2.2"
        private const val FIRESTORE_EMULATOR_PORT = 9099
    }
}
