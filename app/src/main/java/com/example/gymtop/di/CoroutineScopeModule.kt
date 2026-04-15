package com.example.gymtop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * CoroutineScopeModule — módulo Hilt que provê o CoroutineScope de nível de aplicação.
 *
 * Por que um módulo dedicado?
 * Separar a criação de escopos de coroutine em seu próprio módulo mantém o código
 * organizado e segue o princípio de responsabilidade única. RepositoryModule cuida
 * de Repositories; este módulo cuida de infraestrutura de concorrência.
 *
 * Detalhes do scope provido:
 *
 *   SupervisorJob():
 *     Garante que o cancelamento ou falha de um filho NÃO cancela os outros filhos.
 *     Ideal para tarefas independentes como o pré-carregamento do JSON.
 *
 *   Dispatchers.Default:
 *     Thread pool de CPU. O dispatcher de execução de cada tarefa filha pode ser
 *     sobrescrito individualmente (ex: LibraryDataSource usa Dispatchers.IO).
 *
 *   @Singleton:
 *     Uma única instância por processo — o scope vive enquanto o app estiver ativo.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
}

