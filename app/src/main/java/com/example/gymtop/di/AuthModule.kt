package com.example.gymtop.di

import com.example.gymtop.data.repository.AuthRepositoryImpl
import com.example.gymtop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AuthModule — módulo Hilt responsável pelas dependências de autenticação.
 *
 * Dividido em dois objetos internos:
 *  - [FirebaseProviders]: usa @Provides para instâncias do Firebase SDK
 *    (não podemos usar @Inject nessas classes pois são de terceiros).
 *  - [Bindings]: usa @Binds para vincular a interface [AuthRepository]
 *    à implementação concreta [AuthRepositoryImpl].
 *
 * Por que separar @Provides e @Binds em classes diferentes?
 * O Hilt exige que @Binds esteja em uma classe abstrata ou interface,
 * enquanto @Provides pode estar em um object. Separá-los evita conflitos.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseProviders {

    /**
     * Fornece a instância singleton do FirebaseAuth.
     * getInstance() retorna sempre o mesmo objeto, o @Singleton garante
     * que o Hilt não o recrie desnecessariamente.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Fornece a instância singleton do FirebaseFirestore.
     * Usado pelo AuthRepositoryImpl para ler/escrever perfis de usuário.
     */
//    @Provides
//    @Singleton
//    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

/**
 * AuthBindings — vincula a interface [AuthRepository] à implementação [AuthRepositoryImpl].
 *
 * @Binds é mais eficiente que @Provides para esse caso: o Hilt sabe que
 * sempre que alguém pedir [AuthRepository], deve usar [AuthRepositoryImpl].
 * O construtor de [AuthRepositoryImpl] tem @Inject, então o Hilt resolve
 * as dependências automaticamente sem precisarmos listá-las aqui.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindings {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}

