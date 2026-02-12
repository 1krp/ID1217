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
#include <errno.h>
#define MAXHONEY 100000
#define MAXBEES 20

int capHoney;
int numBees;
int sum = 0;
int honeyProduced[MAXBEES+1];
int rCounter = 1;

sem_t portHoney;
sem_t deposit;
sem_t honeyFull;

void *honeyBee(void *arg){
    long myid = (long) arg;
    while(1){
        sem_wait(&deposit);
        if(sem_trywait(&portHoney)==0){
            //Acquierd
           // printf("bee %ld has depoed\n",myid);
            honeyProduced[myid]++;
            int val;
            sem_getvalue(&portHoney, &val);
            //Last honey
            if(val == 0){
                //printf("Bee %ld has woken up the bear \n",myid);
                sem_post(&honeyFull);
                sem_post(&deposit);
            } else {
                sem_post(&deposit); 
            }
        } else {
            if (errno == EAGAIN) {
                sem_post(&deposit);
            } else {
                // trywait error
                perror("sem_trywait");
            }
        }
        //sleep(rand() % 2 + 1);    
    }
};

void *bear(void *){
    while(1){
        sem_wait(&honeyFull);
        //sem_post(&bearEating);
        sem_wait(&deposit);
        for(int i = 0; i < capHoney; i++){
            sem_post(&portHoney);
        }
        
        //printf("Bear ate %d units of honey.\n",capHoney);
         if(rCounter > 15){
            exit(0);
        }
        printf("round nr = %d\n", rCounter);
        rCounter++;

       
        //printf("honey distribuition \n");
        for (int i = 1; i < numBees+1; i++) {
            printf("[bee %d : %d honey]\n",i, honeyProduced[i]);
            sum += honeyProduced[i];
        }
        //printf("\nTotal honey %d\n", sum);
        sum = 0;

        sleep(1);
        sem_post(&deposit);
    }
};


int main(int argc, char *argv[]){
    long l;
    
    capHoney = (argc > 1)? atoi(argv[1]) : MAXHONEY;
    if (capHoney > MAXHONEY) capHoney = MAXHONEY;
    numBees = (argc > 1)? atoi(argv[2]) : MAXBEES;
    if (numBees > MAXBEES) numBees = MAXBEES;

    sem_init(&portHoney, 0, capHoney);
    sem_init(&deposit, 0, 1);
    sem_init(&honeyFull, 0, 0);

    pthread_attr_t attr;
    pthread_t beeId[MAXBEES];
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    pthread_create(&beeId[0], &attr, bear, (void *) 0);

    printf("init Bees \n");
    for (l = 1; l < numBees+1; l++) {
        pthread_create(&beeId[l], &attr, honeyBee, (void *) l);
    }
    pthread_exit(NULL);

}
