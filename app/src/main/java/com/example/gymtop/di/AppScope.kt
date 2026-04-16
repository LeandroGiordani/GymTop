package com.example.gymtop.di

import javax.inject.Qualifier

/**
 * @ApplicationScope — qualificador Hilt para um CoroutineScope com vida útil do app.
 *
 * Por que um qualificador?
 * Hilt pode precisar injetar múltiplos CoroutineScopes (ex: um por ViewModel, um global).
 * O qualificador diferencia qual instância deve ser injetada em cada ponto.
 *
 * Uso:
 *   @Inject constructor(@ApplicationScope private val appScope: CoroutineScope)
 *
 * Ciclo de vida:
 * O scope é instalado no SingletonComponent, portanto vive enquanto o processo do app
 * estiver ativo. Ideal para tarefas de inicialização (como pré-carregamento do JSON
 * de exercícios em LibraryDataSource) que não estão atreladas a nenhuma tela.
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

