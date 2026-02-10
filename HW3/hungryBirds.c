#ifndef _REENTRANT 
#define _REENTRANT 
#endif 
#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <sys/time.h>
#include <semaphore.h>
#include <unistd.h>
#define MAXWORMS 1000
#define MAXBIRDS 15

int numWorms;
int numBabyBirds;

int wormsEaten[MAXBIRDS+1];

sem_t worms;
sem_t eatSem;
sem_t chirp;

void *babyBird(void *arg){
    long myid = (long) arg;
    while(1){
        sem_wait(&eatSem);
        printf("Baby bird %ld is checking for worms.\n", (long) arg);
       // printf("worms trywait %d \n", sem_trywait(&worms));
        if(sem_trywait(&worms) == 0){
            // Acquierd
            printf("Baby bird %ld is eating a worm.\n", (long) arg);
            wormsEaten[myid]++;
            sem_post(&eatSem);
            printf("Baby bird %ld is sleeping.\n", (long) arg);
            sleep(rand() % 2 + 1);
        } else {
            printf("Baby bird %ld found that there are no worms, chirps loudly.\n", (long) arg);
            sem_post(&chirp);
            sem_post(&eatSem);
            printf("Baby bird %ld is sleeping.\n", (long) arg);
            sleep(rand() % 1 + 1);
        }
    }
};

void *parentBird(void *){
    while(1){
        sem_wait(&chirp);
        sem_wait(&eatSem);
        
        for(int i = 0; i < numWorms; i++){
            sem_post(&worms);
        }

        printf("worm distribuition \n");
        for (int i = 1; i < numBabyBirds+1; i++) {
            printf("bird %d [%d worms]\n",i, wormsEaten[i]);
        }

        int val;
        sem_getvalue(&worms, &val);
        printf("Parent is refilling with %d worms.\n",val);
        sem_post(&eatSem);
    }
};

int main(int argc, char *argv[]){
    long l;
    
    numWorms = (argc > 1)? atoi(argv[1]) : MAXWORMS;
    if (numWorms > MAXWORMS) numWorms = MAXWORMS;
    numBabyBirds = (argc > 1)? atoi(argv[2]) : MAXBIRDS;
    if (numBabyBirds > MAXBIRDS) numBabyBirds = MAXBIRDS;

    sem_init(&worms, 0, numWorms);
    sem_init(&eatSem, 0, 1);
    sem_init(&chirp, 0, 0);

    pthread_attr_t attr;
    pthread_t birdId[MAXBIRDS];
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);
    pthread_create(&birdId[0], &attr, parentBird, (void *) 0);

    printf("#worms = %d \n", numWorms);

    int val;
    sem_getvalue(&worms, &val);
    printf("#SEMworms = %d \n", val);

    printf("init birds \n");
    for (l = 1; l < numBabyBirds+1; l++) {
        pthread_create(&birdId[l], &attr, babyBird, (void *) l);
    }
    pthread_exit(NULL);
}