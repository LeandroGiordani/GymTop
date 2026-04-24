package com.example.gymtop.di

import com.example.gymtop.data.datasource.LibraryDataSource
import com.example.gymtop.data.repository.ExerciseRepositoryImpl
import com.example.gymtop.data.repository.SetRepositoryImpl
import com.example.gymtop.data.repository.WorkoutRepositoryImpl
import com.example.gymtop.domain.repository.ExerciseRepository
import com.example.gymtop.domain.repository.SetRepository
import com.example.gymtop.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RepositoryModule - Módulo Hilt que fornece as dependências dos Repositories
 *
 * @Module: Marca como um módulo Hilt.
 * @InstallIn(SingletonComponent::class): As dependências serão Singletons.
 *
 * Por que usar @Binds em vez de @Provides?
 * - @Binds é mais eficiente: gerado em tempo de compilação sem criar um método de fábrica.
 * - Expressivo: declara claramente "quando alguém pedir X, dê Y".
 * - Exige que o módulo seja abstract — Hilt gera a implementação automaticamente.
 *
 * Padrão MVVM:
 * Este módulo está na camada Data Layer - configura como criar Repositories.
 * ViewModels (Presentation Layer) não sabem como Repositories são criados.
 * Isso permite trocar implementação do Repository sem alterar ViewModels.
 *
 * Benefício para Kotlin Multiplatform:
 * iOS pode ter um módulo equivalente com implementações específicas da plataforma,
 * sem alterar nada no código dos ViewModels compartilhados.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Vincula [WorkoutRepositoryImpl] à interface [WorkoutRepository].
     * Hilt injeta [WorkoutRepositoryImpl] onde quer que [WorkoutRepository] seja solicitado.
     * O @Inject constructor de [WorkoutRepositoryImpl] fornece as dependências automaticamente.
     */
    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        impl: WorkoutRepositoryImpl
    ): WorkoutRepository

    /**
     * Vincula [ExerciseRepositoryImpl] à interface [ExerciseRepository].
     * Hilt injeta [ExerciseRepositoryImpl] onde quer que [ExerciseRepository] seja solicitado.
     */
    @Binds
    @Singleton
    abstract fun bindExerciseRepository(
        impl: ExerciseRepositoryImpl
    ): ExerciseRepository

    /**
     * Vincula [SetRepositoryImpl] à interface [SetRepository].
     * Hilt injeta [SetRepositoryImpl] onde quer que [SetRepository] seja solicitado.
     */
    @Binds
    @Singleton
    abstract fun bindSetRepository(
        impl: SetRepositoryImpl
    ): SetRepository
}
