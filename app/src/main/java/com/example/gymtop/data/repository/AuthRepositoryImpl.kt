package com.example.gymtop.data.repository

import com.example.gymtop.domain.model.Gender
import com.example.gymtop.domain.model.User
import com.example.gymtop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * AuthRepositoryImpl — implementação concreta do [AuthRepository] usando Firebase.
 *
 * Dependências injetadas pelo Hilt (fornecidas por [AuthModule]):
 * @param firebaseAuth    Instância do Firebase Auth — gerencia credenciais e sessão.
 * @param firestore       Instância do Firestore — armazena o perfil do usuário.
 *
 * Por que Firestore além do Auth?
 * O Firebase Auth guarda apenas uid, email e displayName.
 * Campos extras como [Gender] precisam de uma coleção Firestore ("users/{uid}").
 *
 * Estrutura Firestore:
 * Collection: "users"
 *   Document: "{uid}"
 *     Fields: name (String), email (String), gender (String)
 */
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    // ── Firestore collection name constant ─────────────────────────────────────
    companion object {
        private const val USERS_COLLECTION = "users"
    }

    // ── getCurrentUser ─────────────────────────────────────────────────────────

    /**
     * Verifica se há uma sessão ativa no Firebase Auth e retorna o [User] do Firestore.
     * Retorna null se não há usuário logado ou se o perfil Firestore não existe.
     */
    override suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return fetchUserProfile(firebaseUser.uid)
    }

    // ── createAccountWithEmail ─────────────────────────────────────────────────

    /**
     * 1. Cria a conta no Firebase Auth com email + senha.
     * 2. Salva o perfil (name, email, gender) no Firestore.
     * 3. Retorna o [User] de domínio no sucesso.
     */
    override suspend fun createAccountWithEmail(
        name: String,
        email: String,
        gender: Gender,
        password: String
    ): Result<User> = runCatching {
        // Firebase Auth: cria a conta e faz login automaticamente
        val authResult = firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

        val uid = authResult.user?.uid
            ?: error("Firebase Auth não retornou UID após criação de conta.")

        // Firestore: salva o perfil com os dados extras (name, gender)
        val profile = mapOf(
            "name"   to name,
            "email"  to email,
            "gender" to gender.name   // armazena o nome do enum (ex: "FEMININO")
        )
        firestore.collection(USERS_COLLECTION).document(uid).set(profile).await()

        User(uid = uid, name = name, email = email, gender = gender)
    }

    // ── signInWithGoogle ───────────────────────────────────────────────────────

    /**
     * Troca o Google ID Token por uma credencial Firebase e autentica.
     * Se for o primeiro login do usuário, cria o perfil no Firestore com os
     * dados disponíveis no Google Account (displayName, email).
     * Gender fica como OUTRO por padrão — o usuário pode editar no perfil.
     *
     * NOTA: o [idToken] deve ser obtido pela Activity/Composable usando
     * GoogleSignInClient e passado para este método.
     */
    override suspend fun signInWithGoogle(idToken: String): Result<User> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = firebaseAuth.signInWithCredential(credential).await()

        val firebaseUser = authResult.user
            ?: error("Firebase não retornou usuário após login com Google.")

        // Verifica se o perfil já existe no Firestore (login recorrente)
        val existingProfile = fetchUserProfile(firebaseUser.uid)
        if (existingProfile != null) return@runCatching existingProfile

        // Primeiro login com Google: cria perfil no Firestore
        val name   = firebaseUser.displayName ?: ""
        val email  = firebaseUser.email ?: ""
        val gender = Gender.OUTRO  // padrão; usuário pode atualizar depois

        val profile = mapOf(
            "name"   to name,
            "email"  to email,
            "gender" to gender.name
        )
        firestore.collection(USERS_COLLECTION).document(firebaseUser.uid).set(profile).await()

        User(uid = firebaseUser.uid, name = name, email = email, gender = gender)
    }

    // ── signOut ────────────────────────────────────────────────────────────────

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    /**
     * Busca o perfil do usuário no Firestore pelo [uid].
     * Mapeia os campos do documento para o modelo de domínio [User].
     * Retorna null se o documento não existir.
     */
    private suspend fun fetchUserProfile(uid: String): User? {
        val doc = firestore.collection(USERS_COLLECTION).document(uid).get().await()
        if (!doc.exists()) return null

        val name   = doc.getString("name") ?: ""
        val email  = doc.getString("email") ?: ""
        val genderStr = doc.getString("gender") ?: Gender.OUTRO.name
        val gender = runCatching { Gender.valueOf(genderStr) }.getOrDefault(Gender.OUTRO)

        return User(uid = uid, name = name, email = email, gender = gender)
    }
}

