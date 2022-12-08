/**
 * @Author: Vinicius Mari Marrafon from Poços de Caldas
 * @Language: C99
 * @OS: Windows NT
 * 
 * @Description: Bounded Buffer Problem (Problema do Produtor-Consumidor)
 *  Dois thread são criadas! Um com o objetivo de preencher um array com valores (Produzir valores)
 * e outro sedento por consumir os valores desse array (Consumir Valores).
 * 
 *  Como se pode imaginar, diversos problemas surgem quando trabalhos com execuçao em paralelo (Threads)
 * e, cabe a nos, gerenciar e garantir a integridade dos dados presentes na chamada "Região Crítica"
 * (Nesse exemplo, façamos o uso de semáforos)
 * 
 * 
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <windows.h>
#include <time.h>

#define     MAX_LEN         100
#define     CONSUMED       -1

void *semaforo;

int produto[MAX_LEN];
int produtoId = 0;

void *__stdcall produtor(void *protocolo)
{
    while (1)
    {
        Sleep((rand() % 5000) + 1);
        if (produtoId < MAX_LEN) {

            // Tenta acessar a região crítica
            DWORD result = WaitForSingleObject(semaforo, 0);

            switch (result)
            {
            case WAIT_OBJECT_0:
                produto[produtoId] = (rand() % 100) + 1;
                printf("Produto [%d]: %d foi produzido!\n", produtoId, produto[produtoId]);
                produtoId++;

                LONG anterior = -1;
                ReleaseSemaphore(semaforo, 1, &anterior);
                // printf("Release: %ld\n", anterior);
                break;

            case WAIT_TIMEOUT:
                break; 
            }
        }
    }

    return (void *)0;
}

void *__stdcall consumidor(void *protocolo)
{
    while (1)
    {
        Sleep((rand() % 5000) + 1);
        if (produtoId > 0) {

            // Tenta acessar a região crítica
            DWORD result = WaitForSingleObject(semaforo, 0);

            switch (result)
            {
            case WAIT_OBJECT_0:
                printf("Produto [%d]: %d foi consumido!\n", produtoId, produto[produtoId - 1]);
                produto[produtoId - 1] = CONSUMED;
                --produtoId; 

                LONG anterior = -1;
                ReleaseSemaphore(semaforo, 1, &anterior);
                //printf("Release: %ld\n", anterior);

                break;
            case WAIT_TIMEOUT:
                break;
            }

        }
    }
    return (void *)0;
}

int main(void)
{
    srand(time((void *)0));
    
    // Atributo de Seguranca
    // Valor inicial do semaforo
    // Valor total de locks
    // null (Nome do semaforo) 
    semaforo = CreateSemaphore(0, 2, 2, 0);

    if (semaforo == (void *)0)  {
        printf("CreateSemaphore error: %lu\n", GetLastError());
        exit(EXIT_FAILURE);
    }

    void *thread1;                   // pthread_t
    void *thread2;

    DWORD thread1Id = 0;
    DWORD thread2Id = 1;

    // Criacao e Execucao de uma Thread - pthread_create
    thread1 = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)produtor, NULL, 0, &thread1Id);
    thread2 = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)consumidor, NULL, 0, &thread2Id);

    if (thread1 != NULL)
    {
        // pthread_join
        WaitForSingleObject(thread1, INFINITE);
    }

    if (thread2 != NULL)
    {
        // pthread_join
        WaitForSingleObject(thread2, INFINITE);
    }

    CloseHandle(thread1);
    CloseHandle(thread2);
}