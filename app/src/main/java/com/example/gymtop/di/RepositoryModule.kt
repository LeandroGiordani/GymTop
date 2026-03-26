package com.example.gymtop.di

import com.example.gymtop.data.dao.ExerciseDao
import com.example.gymtop.data.dao.SetDao
import com.example.gymtop.data.dao.WorkoutDao
import com.example.gymtop.data.datasource.LibraryDataSource
import com.example.gymtop.data.repository.ExerciseRepository
import com.example.gymtop.data.repository.SetRepository
import com.example.gymtop.data.repository.WorkoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RepositoryModule - Módulo Hilt que fornece as dependências dos Repositories
 *
 * @Module: Marca como um módulo Hilt
 * @InstallIn(SingletonComponent::class): As dependências serão Singletons
 *
 * Padrão Repository + Hilt:
 * Este módulo centraliza a criação dos Repositories.
 * ViewModels recebem Repositories via injeção, não precisam criar manualmente.
 *
 * Padrão MVVM:
 * Este módulo está na camada Data Layer - configura como criar Repositories.
 * ViewModels (Presentation Layer) não sabem como Repositories são criados.
 * Isso permite trocar implementação do Repository sem alterar ViewModels.
 *
 * Benefício para Kotlin Multiplatform:
 * Quando expandir para iOS, pode-se criar RepositoryModule similar no iOS
 * com implementações específicas, sem alterar código dos ViewModels.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Fornece a instância do WorkoutRepository como Singleton
     *
     * Hilt injeta automaticamente o WorkoutDao necessário (fornecido por DatabaseModule)
     */
    @Provides
    @Singleton
    fun provideWorkoutRepository(
        workoutDao: WorkoutDao
    ): WorkoutRepository {
        return WorkoutRepository(workoutDao)
    }

    /**
     * Fornece a instância do ExerciseRepository como Singleton.
     *
     * Recebe duas dependências injetadas pelo Hilt:
     * - exerciseDao: fornecido por DatabaseModule
     * - libraryDataSource: @Singleton com @Inject constructor, Hilt resolve automaticamente
     *
     * O ExerciseRepository combina as duas fontes para montar o Exercise (domain model).
     */
    @Provides
    @Singleton
    fun provideExerciseRepository(
        exerciseDao: ExerciseDao,
        libraryDataSource: LibraryDataSource
    ): ExerciseRepository {
        return ExerciseRepository(exerciseDao, libraryDataSource)
    }

    /**
     * Fornece a instância do SetRepository como Singleton
     *
     * Hilt injeta automaticamente o SetDao necessário (fornecido por DatabaseModule)
     */
    @Provides
    @Singleton
    fun provideSetRepository(
        setDao: SetDao
    ): SetRepository {
        return SetRepository(setDao)
    }

    /**
     * TODO: Quando adicionar mais Repositories, criar funções @Provides aqui
     * @Provides
     * @Singleton
     * fun provideOtherRepository(
     *     otherDao: OtherDao
     * ): OtherRepository {
     *     return OtherRepository(otherDao)
     * }
     */
}

