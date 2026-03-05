package com.example.gymtop.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gymtop.data.dao.ExerciseDao
import com.example.gymtop.data.dao.WorkoutDao
import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.data.entity.WorkoutEntity

/**
 * AppDatabase - Classe abstrata que representa o banco de dados Room
 *
 * @Database: Anotação que define esta classe como um banco de dados Room.
 * Deve especificar:
 * - entities: lista de entidades (tabelas) no banco de dados
 * - version: número da versão (incrementar quando modificar schema)
 *
 * A classe deve ser abstrata e herdar de RoomDatabase.
 * Room implementará automaticamente o acesso aos DAOs através de métodos abstratos.
 *
 * Padrão MVVM: AppDatabase está no Data Layer - é a ponte entre a app e o SQLite.
 * É implementado como Singleton (uma única instância durante toda a execução do app)
 * através do Hilt (veremos em DatabaseModule.kt).
 *
 * Migrations:
 * Quando mudar o schema do banco de dados (adicionar/remover campos),
 * deve-se incrementar a 'version' e definir migrations para não perder dados.
 * Por enquanto, utilizamos fallbackToDestructiveMigration (apaga e recria - apenas para dev).
 */
@Database(
    entities = [WorkoutEntity::class, ExerciseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Função abstrata que retorna a instância do WorkoutDao.
     * Room implementa automaticamente este método.
     */
    abstract fun workoutDao(): WorkoutDao

    /**
     * Função abstrata que retorna a instância do ExerciseDao.
     * Room implementa automaticamente este método.
     */
    abstract fun exerciseDao(): ExerciseDao

    /**
     * TODO: Quando adicionar mais DAOs, criar funções abstratas aqui
     * abstract fun otherDao(): OtherDao
     */
}

