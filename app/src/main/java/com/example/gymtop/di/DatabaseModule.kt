package com.example.gymtop.di

import android.content.Context
import androidx.room.Room
import com.example.gymtop.data.database.AppDatabase
import com.example.gymtop.data.dao.WorkoutDao
import com.example.gymtop.data.dao.ExerciseDao
import com.example.gymtop.data.dao.SetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DatabaseModule - Módulo Hilt que fornece as dependências relacionadas ao banco de dados
 *
 * @Module: Anotação que marca esta classe como um módulo Hilt.
 * Módulos definem como criar e injetar dependências.
 *
 * @InstallIn(SingletonComponent::class): Define que as dependências serão Singletons
 * (uma única instância durante toda a execução do app).
 *
 * Padrão Hilt:
 * Hilt usa Módulos para centralizar a criação de objetos complexos.
 * Cada função com @Provides diz como criar uma dependência.
 * Hilt injeta automaticamente as dependências onde são necessárias.
 *
 * Padrão MVVM:
 * Este módulo está no Data Layer - configura como criar a camada de dados.
 * ViewModels não sabem como AppDatabase ou DAOs são criados - apenas usam.
 * Isso permite trocar implementações sem modificar ViewModels.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Fornece a instância do AppDatabase como Singleton.
     *
     * @Provides: Diz ao Hilt como criar uma instância de AppDatabase
     * @Singleton: Garante que apenas uma instância será criada durante toda a execução
     * @ApplicationContext: Context da aplicação (não de Activity específica)
     *
     * fallbackToDestructiveMigration():
     * Por enquanto, se o schema mudar, o banco será recriado (perdendo dados).
     * Em produção, implementar migrations apropriadas.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gymtop_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Fornece o WorkoutDao extraído do AppDatabase
     *
     * Hilt automaticamente injeta o AppDatabase (fornecido acima)
     * e chama workoutDao() para retornar o DAO
     */
    @Provides
    @Singleton
    fun provideWorkoutDao(database: AppDatabase): WorkoutDao {
        return database.workoutDao()
    }

    /**
     * Fornece o ExerciseDao extraído do AppDatabase
     */
    @Provides
    @Singleton
    fun provideExerciseDao(database: AppDatabase): ExerciseDao {
        return database.exerciseDao()
    }

    /**
     * Fornece o SetDao extraído do AppDatabase
     */
    @Provides
    @Singleton
    fun provideSetDao(database: AppDatabase): SetDao {
        return database.setDao()
    }

    /**
     * TODO: Quando adicionar mais DAOs, criar funções @Provides aqui
     * @Provides
     * @Singleton
     * fun provideOtherDao(database: AppDatabase): OtherDao {
     *     return database.otherDao()
     * }
     */
}

