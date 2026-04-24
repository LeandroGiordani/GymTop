package com.example.gymtop.domain.repository

import com.example.gymtop.domain.model.Gender
import com.example.gymtop.domain.model.User

/**
 * AuthRepository — contrato de autenticação da camada de domínio.
 *
 * A interface desacopla a lógica de negócio da implementação concreta (Firebase).
 * O ViewModel depende DESTA interface, não de [AuthRepositoryImpl], permitindo:
 * - Trocar Firebase por outro backend sem alterar ViewModels
 * - Criar um FakeAuthRepository para testes unitários
 *
 * Todas as operações são suspend functions pois envolvem I/O de rede.
 * Retornam [Result] para que o ViewModel trate sucesso e falha de forma segura.
 */
interface AuthRepository {

    /**
     * Retorna o usuário autenticado atualmente, ou null se não há sessão ativa.
     * Usado pela SplashScreen para decidir para onde navegar ao abrir o app.
     */
    suspend fun getCurrentUser(): User?

    /**
     * Cria uma conta nova com e-mail e senha.
     * Além de criar o usuário no Firebase Auth, salva [name] e [gender] no Firestore,
     * já que o Auth nativo não possui campo de gênero.
     *
     * @return [Result.success] com o [User] criado, ou [Result.failure] com a exceção.
     */
    suspend fun createAccountWithEmail(
        name: String,
        email: String,
        gender: Gender,
        password: String
    ): Result<User>

    /**
     * Autentica usando um Google ID Token obtido pelo GoogleSignInClient.
     * O token é trocado por uma credencial do Firebase Auth.
     * Se for o primeiro login, o perfil do usuário é criado no Firestore.
     *
     * @param idToken Token gerado pelo Google Sign-In (obtido na Activity/Composable).
     * @return [Result.success] com o [User], ou [Result.failure] com a exceção.
     */
    suspend fun signInWithGoogle(idToken: String): Result<User>

    /**
     * Encerra a sessão do usuário atual no Firebase Auth.
     */
    suspend fun signOut()
}

