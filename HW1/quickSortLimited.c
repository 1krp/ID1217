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
#include <stdbool.h>
#include <pthread.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <sys/time.h>
#define MAXSIZE 100000000  /* maximum matrix size */
#define MAXTHREADS 32   /* maximum number of threads */

int numThreads = 0;
pthread_mutex_t numthreads_m;
int maxThreads;

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

/*  shared variable  */



/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
  int i, j;
  long l; /* use long in case of a 64-bit system */



  /* set global thread attributes */

  /* initialize mutex and condition variable */
  pthread_mutex_init(&numthreads_m, NULL);

  size = (argc > 1)? atoi(argv[1]) : MAXSIZE;
  maxThreads = (argc > 2)? atoi(argv[2]) : MAXTHREADS;
  if (size > MAXSIZE) size = MAXSIZE;
  if (maxThreads > MAXTHREADS) maxThreads = MAXTHREADS;

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
  printf("The execution time is %g sec\n", end_time - start_time);
  
  #ifdef DEBUG //sorted arr
  for (int i = 0; i < size; i++) {
    printf("%d ",array[i]);
  }
  #endif
  printf("size: %d\n", size);

  printf("numThreads: %d\n", numThreads);
  
  pthread_exit(NULL);
}

/* Each worker sums the values in one strip of the matrix.
   After a barrier, worker(0) computes and prints the total */

void *quickSortThread(void *arg) {
    args *a = (args *)arg;

    quickSort(a->org, a->start, a->end);
    free(a);  
}

int threads(){   
  int check = 0;
  pthread_mutex_lock(&numthreads_m);
  if (numThreads < maxThreads) {
    numThreads++;
    check = 1;
  }
  pthread_mutex_unlock(&numthreads_m);
  return check;
}

void quickSort(int *org, int start, int end) {
  if (end <= start) {
      return;
  }                   //base case
  int pivot = partition(org, start, end);

  if(threads()) {
    pthread_t t;
    pthread_attr_t attr;
    pthread_attr_init(&attr);

    args *left = malloc(sizeof(args));
    left->org = org;
    left->start = start;
    left->end = pivot -1;

    pthread_create(&t, &attr, quickSortThread, left); //left
    quickSort(org, pivot + 1, end);   //right

    pthread_join(t, NULL); 
  } else {
    //seq
    quickSort(org, start, pivot - 1);
    quickSort(org, pivot + 1, end);
  }
  #ifdef DEBUG // pivot, start, end
  printf("pivot point: %d, start: %d, end: %d \n", pivot, start, end);
  #endif
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

