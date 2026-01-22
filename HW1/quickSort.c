/* matrix summation using pthreads

   features: uses a barrier; the Worker[0] computes
             the total sum from partial sums computed by Workers
             and prints the total sum to the standard output

   usage under Linux:
     gcc matrixSum.c -lpthread
     a.out size numWorkers

*/
#ifndef _REENTRANT 
#define _REENTRANT 
#endif 
#include <pthread.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <sys/time.h>
#define MAXSIZE 10000  /* maximum matrix size */
//#define MAXWORKERS 10   /* maximum number of workers */

pthread_mutex_t barrier;  /* mutex lock for the barrier */
pthread_cond_t go;        /* condition variable for leaving */
int numWorkers;           /* number of workers */ 
int numArrived = 0;       /* number who have arrived */

int numThreads = 0;
pthread_mutex_t numthreads_m;

/* a reusable counter barrier */
void Barrier() {
  pthread_mutex_lock(&barrier);
  numArrived++;
  if (numArrived == numWorkers) {
    numArrived = 0;
    pthread_cond_broadcast(&go);
  } else
    pthread_cond_wait(&go, &barrier);
  pthread_mutex_unlock(&barrier);
}

/* timer */
double read_timer() {
    static bool initialized = false;
    static struct timeval start;
    struct timeval end;
    if( !initialized )
    {
        gettimeofday( &start, NULL );
        initialized = true;
    }
    gettimeofday( &end, NULL );
    return (end.tv_sec - start.tv_sec) + 1.0e-6 * (end.tv_usec - start.tv_usec);
}

typedef struct {
    int *org;
    int start;
    int end;
} args;

 
double start_time, end_time; /* start and end times */
int size, stripSize;  /* assume size is multiple of numWorkers */

int* array;

/*  sharded variable  */

void *Worker(void *);



/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
  int sizes[]={10,100,1000,10000};
  for(int loop = 0; loop < 4; loop++){
    size = sizes[loop];
    int i=0; 
    int j=0;
    long l=0; /* use long in case of a 64-bit system */



    /* set global thread attributes */

    /* initialize mutex and condition variable */
    pthread_mutex_init(&barrier, NULL);
    pthread_cond_init(&go, NULL);

    pthread_mutex_init(&numthreads_m, NULL);
    /*
    size = (argc > 1)? atoi(argv[1]) : MAXSIZE;
    if (size > MAXSIZE) size = MAXSIZE;
    */

    /* read command line args if any */

    /* initialize the array */
      array = malloc(size * sizeof(int));
      for (i = 0; i < size; i++) {
            array[i] = rand()%size+1;
      }
  #ifdef DEBUG //unsorted arr
      for (int i = 0; i < size; i++) {
        printf("%d ",array[i]);
      }
      printf("\n");
  #endif

    /* do the parallel work: create the workers */
    start_time = read_timer();

    quickSort(array,0 , size-1);
    /* get end time */
    end_time = read_timer();
    /* print results */
    printf("size: %d,",size);
    printf("#threads: %d,",numThreads);
    printf("The execution time is %g sec \n", end_time - start_time);
    
    numThreads=0;

    #ifdef DEBUG //sorted arr
    for (int i = 0; i < size; i++) {
      printf("%d ",array[i]);
    }
    #endif
    

    
    
    //pthread_exit(NULL);
  }
}

/* Each worker sums the values in one strip of the matrix.
   After a barrier, worker(0) computes and prints the total */

void *quickSortThread(void *arg) {
    args *a = (args *)arg;

    quickSort(a->org, a->start, a->end);
    free(a);  
}


void quickSort(int *org, int start, int end) {
  if (end <= start) {
      return;
  }                   //base case
  int pivot = partition(org, start, end);

  pthread_t t;
  pthread_attr_t attr;
  pthread_attr_init(&attr);

  args *left = malloc(sizeof(args));
  left->org = org;
  left->start = start;
  left->end = pivot -1;

  pthread_create(&t, &attr, quickSortThread, left); //left
  quickSort(org, pivot + 1, end);   //right

  pthread_mutex_lock(&numthreads_m);
  numThreads++;
  pthread_mutex_unlock(&numthreads_m);

  #ifdef DEBUG // pivot, start, end
  printf("pivot point: %d, start: %d, end: %d \n", pivot, start, end);
  #endif

  pthread_join(t, NULL); 
}

int partition(int *org, int start, int end) {
  int pivot = org[end];
  int i = start -1;

  for (int j = start; j <= end -1; j++) {
      if(org[j] < pivot) {
          i++;
          int temp = org[i];
          org[i] = org[j];
          org[j] = temp;
      }
  }
  i++;
  int temp = org[i];
  org[i] = org[end];
  org[end] = temp;
  return i;
}

